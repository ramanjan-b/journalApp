package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepo jrepo;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntity je, String username) {
        try {

            User uname = userService.findByUserName(username);
            je.setDate(LocalDateTime.now());
            JournalEntity saved = jrepo.save(je);

            uname.getJournalEntries().add(saved);

            userService.saveEntry(uname);   // ✅ FIXED
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveEntry(JournalEntity je) {
        jrepo.save(je);
    }

    public List<JournalEntity> getAll() {
        return jrepo.findAll();
    }

    public Optional<JournalEntity> findById(ObjectId id) {
        return jrepo.findById(id);
    }

    @Transactional
    public void deleteById(ObjectId id, String username) {

        User uname = userService.findByUserName(username);
        boolean removed = uname.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if (removed) {
            userService.saveNewUser(uname);
            jrepo.deleteById(id);
        }
    }

    public void deleteAll() {
        jrepo.deleteAll();
    }
}
