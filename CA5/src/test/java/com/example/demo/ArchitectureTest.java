package com.example.demo;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.example.demo")
public class ArchitectureTest {

    @ArchTest
    static final ArchRule layered_architecture_should_be_respected =
            layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()

                    .layer("Controller").definedBy("..controller..")
                    .layer("Service").definedBy("..service..")
                    .layer("Repository").definedBy("..repository..")

                    .whereLayer("Controller")
                    .mayNotBeAccessedByAnyLayer()

                    .whereLayer("Service")
                    .mayOnlyBeAccessedByLayers("Controller")

                    .whereLayer("Repository")
                    .mayOnlyBeAccessedByLayers("Service")

                    .whereLayer("Repository")
                    .mayNotAccessAnyLayer()

                    .whereLayer("Controller")
                    .mayOnlyAccessLayers("Service")

                    .whereLayer("Service")
                    .mayOnlyAccessLayers("Repository");

    @ArchTest
    static final ArchRule repositories_should_be_named_repository =
            classes().that()
                    .resideInAPackage("..repository..")
                    .should()
                    .haveSimpleNameEndingWith("Repository");

    @ArchTest
    static final ArchRule controllers_should_reside_in_controller_package =
            classes().that()
                    .areAnnotatedWith(org.springframework.stereotype.Controller.class)
                    .or()
                    .areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                    .should()
                    .resideInAPackage("..controller..");

    @ArchTest
    static final ArchRule services_should_not_use_field_injection =
            noFields().that()
                    .areDeclaredInClassesThat()
                    .resideInAPackage("..service..")
                    .should()
                    .beAnnotatedWith(org.springframework.beans.factory.annotation.Autowired.class);

}
