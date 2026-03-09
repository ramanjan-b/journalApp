package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    JournalEntryService jeserv;
    @Autowired
    private UserService userService;

    //    private Map<String,JournalEntity> journalEntries=new HashMap<>();
//@GetMapping
//    public List<JournalEntity> getAll(){
//    return jeserv.getAll();
//    }
    @GetMapping
    public ResponseEntity<?> getByUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User uname = userService.findByUserName(username);
        List<JournalEntity> all = uname.getJournalEntries();
        if (all != null && !all.isEmpty()) {

            return new ResponseEntity<>(all, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntity> getById(@PathVariable ObjectId myid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUserName(username);
        List<JournalEntity> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntity> je = jeserv.findById(myid);
            if (je.isPresent()) {
                return new ResponseEntity<>(je.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntity> unameentry(@RequestBody JournalEntity je) {


        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            jeserv.saveEntry(je, username);
            return new ResponseEntity<JournalEntity>(je, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<JournalEntity>(HttpStatus.BAD_REQUEST);
        }

    }

    //    @PostMapping
//    public ResponseEntity<JournalEntity> entry(@RequestBody JournalEntity je){
//    try{
//        je.setDate(LocalDateTime.now());
//        jeserv.saveEntry(je);
//        return new ResponseEntity<JournalEntity>(je,HttpStatus.CREATED);
//    }
//    catch(Exception e){
//            return new ResponseEntity<JournalEntity>(HttpStatus.BAD_REQUEST);
//        }
//
//}
    @PutMapping("id/{myid}")
    public ResponseEntity<JournalEntity> updateById(@PathVariable ObjectId myid,
                                                    @RequestBody JournalEntity newEntry
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findByUserName(username);
        List<JournalEntity> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntity> je = jeserv.findById(myid);
            if (je.isPresent()) {
                JournalEntity oldEntry = je.get();

                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ?
                        newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.equals("") ?
                        newEntry.getContent() : oldEntry.getContent());
                jeserv.saveEntry(oldEntry);
                return new ResponseEntity<>(je.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        jeserv.deleteById(myId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public void deleteById() {
        jeserv.deleteAll();
    }
}
