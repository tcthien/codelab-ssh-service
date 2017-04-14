package com.tts.codelab.ssh.repository;

import com.tts.codelab.ssh.domain.VncSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VncSessionRepository extends CrudRepository<VncSession, String> {
}
