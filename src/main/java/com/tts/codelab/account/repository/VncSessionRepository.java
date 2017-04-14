package com.tts.codelab.account.repository;

import com.tts.codelab.account.domain.VncSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VncSessionRepository extends CrudRepository<VncSession, String> {
}
