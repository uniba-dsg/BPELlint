package bpellint;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class RulesWithMoreThanOneViolationPrinter {

    public static void main(String[] args) {
        List<Object[]> elems = new LinkedList<>();
        elems.addAll(SAViolationTestData.saViolationTests());
        elems.addAll(XsdSchemaValidatorTests.data());

        System.out.println("ALL=" + elems.stream().count());
        System.out.println("ALL-EXPERIMENTAL=" + elems.stream().filter(noExperimentals()).count());
        System.out.println("ALL-IGNORE_ME=" + elems.stream().filter(noIgnoredTests()).count());
        System.out.println("ALL-EXPERIMENTAL-IGNORE_ME=" + elems.stream().filter(noExperimentals()).filter(noIgnoredTests()).count());
        System.out.println("ALL-EXPERIMENTAL-IGNORE_ME more than one SA rule=" + elems.stream().filter(noExperimentals()).filter(noIgnoredTests()).filter(moreThanOneSAviolation()).count());

        System.out.println("PRINT ALL for ALL-EXPERIMENTAL-IGNORE_ME more than one SA rule=");
        elems.stream().filter(noExperimentals()).filter(noIgnoredTests()).filter(moreThanOneSAviolation()).forEach((s) -> System.out.println(s[0] + " " + s[1]));
    }

    private static Predicate<Object[]> moreThanOneSAviolation() {
        return o -> (o.length > 1 && o[1].toString().contains(","));
    }

    private static Predicate<Object[]> noIgnoredTests() {
        return o -> !o[0].toString().contains("IGNORE_ME");
    }

    private static Predicate<Object[]> noExperimentals() {
        return o -> !(o[0].toString().contains("SA00021") || o[0].toString().contains("SA00060") || o[0].toString().contains("SA00056") || o[0].toString().contains("SA00077"));
    }

}
