package com.example.examplemod.magic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

/** Constructed values, not historical constructor arguments; capture only before migration. */
public final class SpellBaselineTest {
	private static Map<String, Object> snapshot() throws ReflectiveOperationException {
		var result = new LinkedHashMap<String, Object>();
		var spells = new ArrayList<Map<String, Object>>();
		for (var spell : SpellDefinition.ALL) {
			var values = new LinkedHashMap<String, Object>();
			for (var component : SpellDefinition.class.getRecordComponents()) {
				values.put(component.getName(), component.getAccessor().invoke(spell));
			}
			values.put("motion", spell.motion());
			values.put("category", MagicCategory.of(spell));
			values.put("flightTicks", spell.flightTicks());
			values.put("flightRange", Double.toString(spell.flightRange()));
			values.put("piercing", spell.piercing());
			values.put("areaDamage", spell.areaDamage());
			values.put("statusTicks", spell.statusTicks());
			values.put("icy", spell.icy());
			values.put("glowColor", spell.glowColor());
			values.put("officialRank", spell.officialRank());
			spells.add(values);
		}
		result.put("spells", spells);
		result.put("primer", MagicBooks.LESSONS);
		result.put("zenith", MagicBooks.ZENITH_LESSONS);
		result.put("combo", "ice_lance_blizzard");
		return result;
	}
	public static void main(String[] args) throws Exception {
		SpellExecutionBindings.validate();
		var gson = new GsonBuilder().setPrettyPrinting().create();
		var actual = gson.toJsonTree(snapshot());
		if (args.length == 1) {
			var target = Path.of(args[0]);
			if (Files.exists(target)) throw new IllegalStateException("Refusing to overwrite frozen baseline");
			Files.createDirectories(target.getParent());
			Files.writeString(target, gson.toJson(actual) + "\n");
		} else {
			try (var stream = SpellBaselineTest.class.getResourceAsStream("/architecture/spell-baseline.json")) {
				if (stream == null) throw new AssertionError("Missing frozen baseline");
				var expected = JsonParser.parseString(new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
				if (!expected.equals(actual)) throw new AssertionError("Effective spell / learning values drifted from baseline");
			}
		}
		if (SpellDefinition.ALL.size() != 102 || MagicBooks.LESSONS.size() != 39 || MagicBooks.ZENITH_LESSONS.size() != 102)
			throw new AssertionError("Catalog count drift");
		System.out.println("Spell baseline: 102 effective definitions, 39 primer lessons and 102 Zenith lessons match.");
	}
}
