package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {
    Logger logger = LoggerFactory.getLogger(PublicController.class);
    @Autowired
    private UserService userv;

    @GetMapping("/healthcheck")
    public String health() {
        log.debug("fsdfgsfsfs");
        logger.warn("HEALTH CHECKED");
        logger.trace("tracing");
        logger.trace("This is a TRACE message");
//        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message {}",new String("Ramanjan"),new String("Banerjee"));
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message","dsdf","fgdgdg",34234,54545,"gdfg",new String("Ramanjan"));
        return "OK successfully hgfhfgth running user.....";

    }

    @PostMapping
    public void createuser(@RequestBody User user) {
        userv.saveEntry(user);
    }
}
