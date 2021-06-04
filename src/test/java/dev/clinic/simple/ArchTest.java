package dev.clinic.simple;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("dev.clinic.simple");

        noClasses()
            .that()
            .resideInAnyPackage("dev.clinic.simple.service..")
            .or()
            .resideInAnyPackage("dev.clinic.simple.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..dev.clinic.simple.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
