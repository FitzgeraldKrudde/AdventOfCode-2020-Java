package nl.krudde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day_04 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Passport> listPassports = parseInput(inputRaw);
        long result = listPassports.stream()
                .filter(Passport::isValid)
                .count();

        return String.valueOf(result);
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Passport> listPassports = parseInput(inputRaw);
        long result = listPassports.stream()
                .filter(Passport::isValidStrict)
                .count();

        return String.valueOf(result);
    }

    private List<Passport> parseInput(List<String> inputRaw) {
        List<Passport> listPassports = new ArrayList<>();
        Passport passport = new Passport();

        for (String line : inputRaw) {
            if (line.length() == 0) {
                listPassports.add(passport);
                passport = new Passport();
                continue;
            }

            String[] nvps = line.split(" ");
            for (String nvp : nvps) {
                String[] nv = nvp.split(":");
                passport.add(nv[0], nv[1]);
            }

        }
        listPassports.add(passport);

        return listPassports;
    }

    static class Passport {
        private final HashMap<String, String> fields = new HashMap<>();

        public String add(String field, String value) {
            return fields.put(field, value);
        }

        boolean isValid() {
            return fields.size() == 8 || (fields.size() == 7 && !fields.containsKey("cid"));
        }

        boolean isValidStrict() {
            if (!isValid()) {
                return false;
            }

            if (!fields.containsKey("byr")
                    || !fields.containsKey("iyr")
                    || !fields.containsKey("eyr")
                    || !fields.containsKey("hgt")
                    || !fields.containsKey("hcl")
                    || !fields.containsKey("ecl")
                    || !fields.containsKey("pid")) {
                return false;
            }

            return (isValidBirthYear() &&
                    isValidIssueYear() &&
                    isValidExpiryYear() &&
                    isValidHeight() &&
                    isValidHairColor() &&
                    isValidEyeColor() &&
                    isValidPassportId());
        }

        private boolean isValidPassportId() {
            String pid = fields.get("pid");
            return pid.matches("[0-9]{9}");
        }

        private boolean isValidEyeColor() {
            String ecl = fields.get("ecl");
            return ecl.matches("amb|blu|brn|gry|grn|hzl|oth");
        }

        private boolean isValidHairColor() {
            String hcl = fields.get("hcl");
            return hcl.matches("#[0-9a-f]{6}");
        }

        private boolean isValidHeight() {
            String hgt = fields.get("hgt");
            if (hgt.length() < 4) {
                return false;
            }
            String hgtScale = hgt.substring(hgt.length() - 2);
            int hgtValue = Integer.parseInt(hgt.substring(0, hgt.length() - 2));
            return switch (hgtScale) {
                case "in" -> hgtValue >= 59 && hgtValue <= 76;
                case "cm" -> hgtValue >= 150 && hgtValue <= 193;
                default -> throw new IllegalStateException("Unexpected value: " + hgtScale);
            };
        }

        private boolean isValidExpiryYear() {
            int eyr = Integer.parseInt(fields.get("eyr"));
            return eyr >= 2020 && eyr <= 2030;
        }

        private boolean isValidIssueYear() {
            int iyr = Integer.parseInt(fields.get("iyr"));
            return iyr >= 2010 && iyr <= 2020;
        }

        private boolean isValidBirthYear() {
            int byr = Integer.parseInt(fields.get("byr"));
            return byr >= 1920 && byr <= 2002;
        }
    }

    // @formatter:off

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_04().main(filename);
    }
}
