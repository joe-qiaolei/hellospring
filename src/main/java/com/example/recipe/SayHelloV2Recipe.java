package com.example.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.internal.lang.NonNull;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.MethodDeclaration;
import org.openrewrite.java.tree.Statement;

public class SayHelloV2Recipe extends Recipe {

    @Option(displayName = "Fully Qualified Class Name",
        description = "A fully-qualified class name indicating which class to add a hello() method.",
        example = "com.yourorg.FooBar")
    @NonNull
    private final String fullyQualifiedClassName;

    public String getFullyQualifiedClassName() {
        return fullyQualifiedClassName;
    }

    // Recipes must be serializable. This is verified by RecipeTest.assertChanged() and RecipeTest.assertUnchanged()
    @JsonCreator
    public SayHelloV2Recipe(@NonNull @JsonProperty("fullyQualifiedClassName") String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    @Override
    public String getDisplayName() {
        return "Say hello v2";
    }

    @Override
    protected JavaIsoVisitor<ExecutionContext> getVisitor() {
        // getVisitor() should always return a new instance of the visitor to avoid any state leaking between cycles
        return new SayHelloVisitor();
    }

    public class SayHelloVisitor extends JavaIsoVisitor<ExecutionContext> {

        private final JavaTemplate helloTemplate =
            JavaTemplate.builder(this::getCursor, "public String sayHelloV2() { return \"Hello v2 #{}!\"; }")
                .build();

        @Override
        public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl,
                                                        ExecutionContext executionContext) {
            J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
            if (classDecl.getType() == null || !classDecl.getType().getFullyQualifiedName()
                .equals(fullyQualifiedClassName)) {
                // We aren't looking at the specified class so return without making any modifications
                return cd;
            }

            // Check if the class already has a method named "hello" so we don't incorrectly add a second "hello" method
            boolean helloV2MethodExists = classDecl.getBody().getStatements().stream()
                .filter(statement -> statement instanceof J.MethodDeclaration)
                .map(J.MethodDeclaration.class::cast)
                .anyMatch(methodDeclaration -> methodDeclaration.getName().getSimpleName().equals("sayHelloV2"));
            if (helloV2MethodExists) {
                return cd;
            }

            Optional<MethodDeclaration> v1 = classDecl.getBody().getStatements().stream().filter(statement -> statement instanceof J.MethodDeclaration)
                .map(J.MethodDeclaration.class::cast)
                .filter(methodDeclaration -> methodDeclaration.getName().getSimpleName().equals("sayHello"))
                .findFirst();

            cd = cd.withBody(
                cd.getBody().withTemplate(
                    helloTemplate,
                    v1.get().getCoordinates().after(),
                    fullyQualifiedClassName
                ));

            return cd;
        }
    }

}
