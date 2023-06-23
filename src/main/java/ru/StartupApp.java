package ru;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.ejb.Singleton;
import ru.libraryService.LibraryService;
import java.util.logging.Logger;

@Startup
@Singleton
public class StartupApp {
    private final Logger logger = Logger.getLogger(StartupApp.class.getName());
    @Inject
    private LibraryService libraryService;

    @PostConstruct
    void init() {
        logger.info("Start migrate:");
        libraryService.migrate();
        logger.info("End migrate.");
    }
}
