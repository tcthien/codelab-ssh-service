package com.tts.codelab.ssh.ssh.execute;

import com.jcraft.jsch.*;
import com.tts.codelab.ssh.ssh.domain.Command;
import com.tts.codelab.ssh.ssh.domain.CompositeCommand;
import com.tts.codelab.ssh.ssh.domain.SSHResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SSHCommandExecutorImpl implements SSHCommandExecutor {

    @Autowired
    SSHConsoleLog consoleLog;

    @Override
    public SSHResult execute(String ipAddress, String userName, String password, Command cmd) throws Exception {
        return execute(ipAddress, userName, password, cmd, null);
    }

    @Override
    public SSHResult execute(String ipAddress, String userName, String password, Command cmd, SSHCommandCallback callback)
            throws Exception {
        return execute(ipAddress, 22, userName, password, cmd, callback);
    }

    @Override
    public SSHResult execute(String ipAddress, int port, String userName, String password, Command cmd, SSHCommandCallback callback)
            throws Exception {
        SSHResult sshResult = null;

        List<Command> cmds = null;
        if (cmd instanceof CompositeCommand) {
            cmds = new ArrayList<>(((CompositeCommand) cmd).getSubCommands());
        } else {
            cmds = Arrays.asList(cmd);
        }

        Session session = null;
        try {

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(userName, ipAddress, port);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();

            for (Command sshCommand : cmds) {
                if (callback == null || (callback != null && callback.preExecute(sshCommand))) {
                    sshResult = executeCommand(ipAddress, session, sshCommand);
                }

                if (callback != null && !callback.postExecute(sshCommand)) {
                    // callback.postExecute returns false => don't execute further
                    break;
                }
            }

        } catch (JSchException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }

        return sshResult;
    }

    protected SSHResult executeCommand(String host, Session session, Command sshCommand) throws JSchException, IOException {
        System.out.println("Execute Command: " + sshCommand.getTextCommand());
        SSHResult sshResult = new SSHResult();

        Channel channel = session.openChannel("exec");
        try {
            ((ChannelExec) channel).setCommand(sshCommand.getTextCommand());
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            StringBuilder sb = new StringBuilder();

            int time = 0;
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    String outputConsole = new String(tmp, 0, i);
                    System.out.println(outputConsole);
                    sb.append(outputConsole);
                    consoleLog.log(host, outputConsole);
                }
                if (channel.isClosed()) {
                    break;
                }
                time++;
                if (sshCommand.getTimeout() == time) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
            sshResult.setExitStatus(channel.getExitStatus());
            sshResult.setOutputText(sb.toString());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
        return sshResult;
    }

    @Override
    public String getLog(String ipAddress) {
        return consoleLog.getLog(ipAddress);
    }
}
