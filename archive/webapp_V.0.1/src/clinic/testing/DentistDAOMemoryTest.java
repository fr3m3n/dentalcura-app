package clinic.testing;

import clinic.entities.Dentist;
import clinic.persistence.DentistDAOMemory;
import clinic.service.DentistService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DentistDAOMemoryTest {
    private final static Logger LOGGER = Logger.getLogger(DentistDAOMemoryTest.class);

    @Test
    void selectAllTest(){

        LOGGER.info("initializing TEST");

        Dentist dentist = new Dentist(6L,"Test","Hack", 1235);
        Dentist dentist2 = new Dentist(11L,"TestList","Hacker", 2323);
        Dentist dentist3 = new Dentist(11L,"Rename","Update",1010);

        DentistService dentistService = new DentistService();
        dentistService.setDentistIDao(new DentistDAOMemory());

        dentistService.insertDentist(dentist);
        dentistService.insertDentist(dentist2);


        Assertions.assertNull(dentistService.insertDentist(dentist3));
        Assertions.assertEquals("Dentist: Rename Update, License no.: 1010", dentistService.updateDentistByID(dentist3).toString());

        Assertions.assertEquals("[Dentist: Test Hack, License no.: 1235, Dentist: Rename Update, License no.: 1010]", dentistService.selectAllDentist().toString());
        dentistService.deleteDentistByID(11L);
        Assertions.assertEquals("[Dentist: Test Hack, License no.: 1235]", dentistService.selectAllDentist().toString());
        Assertions.assertNull(dentistService.updateDentistByID(dentist2));
        LOGGER.info("Test OK!");

    }
}