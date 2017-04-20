package com.tts.codelab.ssh.repository;

import com.tts.codelab.ssh.domain.VagrantBoxSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VagrantBoxSessionRepository extends CrudRepository<VagrantBoxSession, String> {
}
