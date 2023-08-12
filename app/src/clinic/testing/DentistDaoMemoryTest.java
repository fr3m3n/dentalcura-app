package clinic.testing;

import clinic.entities.Dentist;
import clinic.persistence.DentistDaoMemory;
import clinic.service.DentistService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DentistDaoMemoryTest {
    private final static Logger LOGGER = Logger.getLogger(DentistDaoMemoryTest.class);

    @Test
    void selectAllTest(){

        LOGGER.info("initializing TEST");

        Dentist dentist = new Dentist(6L,"Test","Hack", 1235);
        Dentist dentist2 = new Dentist(11L,"TestList","Hacker", 2323);

        DentistService dentistService = new DentistService();
        dentistService.setDentistIDao(new DentistDaoMemory());

        dentistService.insertDentist(dentist);
        dentistService.insertDentist(dentist2);


        Assertions.assertEquals("[Dentist: Test Hack, License no.: 1235, Dentist: TestList Hacker, License no.: 2323]", dentistService.selectAllDentist().toString());
        LOGGER.info("Test OK!");

    }



}