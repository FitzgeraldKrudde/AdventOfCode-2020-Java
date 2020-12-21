package nl.krudde;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Day_21 extends Day {

    @Override
    public String doPart1(List<String> inputRaw) {
        List<Food> foodList = parseInput(inputRaw);

        List<String> allergenFreeIngredients = determineAllergenFreeIngredients(foodList);

        long result = foodList.stream()
                .flatMap(food -> food.ingredients.stream())
                .filter(allergenFreeIngredients::contains)
                .count();

        return String.valueOf(result);
    }

    private List<String> determineAllergenFreeIngredients(List<Food> foodList) {
        // stream all distinct foods and remove the foods which might contain an allergen
        // i.e.
        // for a food stream all distinct allergens and check if the allergen might be in this food by
        // checking if there are any foods which DOES contain this allergen but does NOT contain this ingredient
        return foodList.stream()
                .flatMap(food -> food.ingredients.stream())
                .distinct()
                .filter(ingredient -> foodList.stream()
                        .flatMap(food -> food.allergens.stream())
                        .distinct()
                        .noneMatch(allergen -> foodList.stream()
                                .noneMatch(food -> food.allergens.contains(allergen) && !food.ingredients.contains(ingredient)))
                )
                .collect(toList());
    }

    @Override
    public String doPart2(List<String> inputRaw) {
        List<Food> foodList = parseInput(inputRaw);

        List<String> allergenFreeIngredients = determineAllergenFreeIngredients(foodList);

        // remove the allergen free ingredients
        List<Food> cleanedFoodList = foodList.stream()
                .map(food -> new Food(
                        food.ingredients.stream()
                                .filter(ingredient -> !allergenFreeIngredients.contains(ingredient))
                                .collect(toList()),
                        food.allergens))
                .collect(toList());

        // map ingredients to allergens
        Map<String, String> mapIngredientToAllergen = mapIngredientsToAllergens(cleanedFoodList);

        return mapIngredientToAllergen.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }

    private Map<String, String> mapIngredientsToAllergens(List<Food> foodList) {
        // start with empty list of mapped ingredients to allergens
        Map<String, String> mappedIngredients = new HashMap<>();

        // collect all ingredients
        List<String> unmappedIngredients = foodList.stream()
                .flatMap(food -> food.ingredients.stream())
                .distinct()
                .collect(toList());

        return mapIngredientsToAllergens(foodList, mappedIngredients, unmappedIngredients);
    }

    private Map<String, String> mapIngredientsToAllergens(List<Food> foodList, Map<String, String> mappedIngredients, List<String> unmappedIngredients) {
        if (unmappedIngredients.size() == 0) {
            return mappedIngredients;
        }

        // pick an unmapped ingredient
        List<String> newUnmappedIngredients = new ArrayList<>(unmappedIngredients);
        String ingredient = newUnmappedIngredients.remove(0);

        // find the possible allergens for this ingredient
        // i.e. the allergens where for all food the food contains the allergen
        List<String> possibleAllergens = foodList.stream()
                .flatMap(food -> food.allergens.stream())
                .distinct()
                .filter(allergen -> foodList.stream()
                        .filter(f -> f.allergens.contains(allergen))
                        .allMatch(f -> f.ingredients.contains(ingredient))
                )
                .collect(Collectors.toList());

        return possibleAllergens.stream()
                .map(allergen -> tryMapping(ingredient, allergen, foodList, mappedIngredients, newUnmappedIngredients))
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    private Map<String, String> tryMapping(String ingredient, String allergen, List<Food> foodList, Map<String, String> mappedIngredients, List<String> unmappedIngredients) {
        // add the mapping ingredient->allergen
        Map<String, String> newMappedIngredients = new HashMap<>(mappedIngredients);
        newMappedIngredients.put(ingredient, allergen);

        // remove this ingredient and allergen from the foodlist
        List<Food> newFoodList = foodList.stream()
                .map(food -> new Food(
                        food.ingredients.stream()
                                .filter(ingr -> !ingr.equals(ingredient))
                                .collect(toList()),
                        food.allergens.stream()
                                .filter(all -> !all.equals(allergen))
                                .collect(toList())))
                .collect(toList());

        return mapIngredientsToAllergens(newFoodList, newMappedIngredients, unmappedIngredients);
    }

    private List<Food> parseInput(List<String> inputRaw) {
        return inputRaw.stream()
                .map(this::parseFood)
                .collect(toList());
    }

    private Food parseFood(String line) {
        String[] fields = line.split("\\s+|, |\\)");
        List<String> ingredients = Arrays.stream(fields)
                .takeWhile(field -> !field.contains("contains"))
                .collect(Collectors.toList());
        List<String> allergens = Arrays.stream(fields)
                .dropWhile(field -> !field.contains("contains"))
                .filter(field -> !"(contains".equals(field))
                .collect(Collectors.toList());
        return new Food(ingredients, allergens);
    }

    // @formatter:off
    record Food(List<String> ingredients, List<String> allergens) {}

    static public void main(String[] args) throws Exception {
        // get our class
        final Class<?> clazz = new Object() {}.getClass().getEnclosingClass();

        // construct filename with input
        final String filename = clazz.getSimpleName().toLowerCase().replace("_0", "_") + ".txt";

        // invoke "main" from the base Day class
        new Day_21().main(filename);
    }
}
