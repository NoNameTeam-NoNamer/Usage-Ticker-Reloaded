# Usage Ticker Reloaded

![Mod Version](https://img.shields.io/badge/version-1.2.1-blue) ![Minecraft](https://img.shields.io/badge/Minecraft-26.1+-green) ![Fabric](https://img.shields.io/badge/Fabric-0.19.3+-yellowgreen) ![Side](https://img.shields.io/badge/Client-side-00bbee) ![Config](https://img.shields.io/badge/Config-Available-purple) [![Modrinth](https://img.shields.io/badge/Modrinth-Download-00AF5C?style=round&logo=modrinth)](https://modrinth.com/mod/usage-ticker-reloaded)

**Usage Ticker Reloaded** is a lightweight client-side Fabric mod that replicates the popular "Usage Ticker" feature from the [Quark mod](https://github.com/VazkiiMods/Quark) for modern Minecraft versions (≥26.1). Since [the original Usage Ticker mod](https://modrinth.com/mod/usage-ticker) hasn't been updated for a long time, I made this similar mod. It displays the icon and total count of the item you're holding, beside your hotbar.

> **Note:** This mod is **client-side only**. It does nothing when installed on a dedicated server.

---

## ✨ Features

-   📦 **Real‑time item count** – Shows the total number of the currently held item (including all stacks in your inventory).
-   👆 **Dual‑hand support** – Both main hand and offhand items are displayed simultaneously (if different).
-   🧹 **Smart deduplication** – If both hands hold the same item, it only shows once with the combined total.
-   🗃️ **Container counting** – Recursively counts matching items inside shulker boxes and bundles, displayed in a separate color above the main counter.
-   🎨 **Fully configurable** – Colors, decimal separator, NBT matching, container scan depth and node limit are all adjustable in-game.
-   🪣 **No world changes** – Adds no blocks, items, recipes, or gameplay mechanics – purely informational.
-   🌐 **Multi-language** – Available in English, Simplified Chinese, Traditional Chinese, Literary Chinese, Japanese, Korean, Russian and French.

---

## ⚙️ Configs

Open via **Mod Menu** (requires [YACL](https://modrinth.com/mod/yacl)).

-   **Decimal separator** – Switch between `.` and `,`
-   **Match NBT** – Only count items whose NBT matches exactly (default: on)
-   **Container depth limit** – Max nesting depth to scan inside containers (0–32, default: 5)
-   **Container node limit** – Max nodes visited during container scan (0–100000, default: 10000)
-   **Main counter color** – RGBA color picker
-   **Container counter color** – RGBA color picker
-   **Debug display text** – Enter a number to preview formatting (e.g. `1234567` → `1.23M`)

---

## 📥 Installation

1.  **Install Fabric Loader** for Minecraft.
2.  **Download the mod JAR** from the [Releases](https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded/releases) page.
3.  Place the JAR in your `mods` folder.
4.  Launch the game and enjoy!

*Requires [Fabric API](https://modrinth.com/mod/fabric-api). Optional: [Mod Menu](https://modrinth.com/mod/modmenu) + [YACL](https://modrinth.com/mod/yacl) for in-game configuration.*

---

## 🛠️ Building from Source

If you want to compile the mod yourself:

```bash
git clone https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded.git
cd Usage-Ticker-Reloaded
./gradlew build
```

The built JAR will be placed in `build/libs/`.

---

## 📋 Compatibility

| Minecraft       | Fabric Loader | Status                                                                      |
|:----------------|:--------------|:----------------------------------------------------------------------------|
| 26.3 SNAPSHOTS  | 0.19.3+       | ❔ Maybe work                                                               |
| 26.2            | 0.19.3+       | ✅ Fully tested                                                             |
| 26.1            | 0.19.3+       | ✅ Fully tested                                                             |
| 1.21.1 or below | -             | ⚠️ You should use [the original mod](https://modrinth.com/mod/usage-ticker) |

If you encounter any incompatibility in the tested versions above, please report it via the issue tracker.

---

## ❓ FAQ

**Q: Will you support versions below 26.1 or other loaders?**  
A: I won't, since I don't have time. However, if you have the ability to do that, feel free to [submit a pull request](https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded/pulls).

**Q: Can I change the position of the counter?**  
A: Position is fixed beside the hotbar. Colors, however, are configurable.

**Q: Does it count items inside shulker boxes and bundles?**  
A: Yes. Container counting is on by default and can be tuned or disabled via the depth/node limits in the config.

**Q: Will my game lag if I carry heavily nested containers?**  
A: No. Container scanning is protected by both a depth limit and a node limit. A `*` prefix is shown when the depth limit is reached, and a `+` suffix is shown when the node limit is reached. Both can appear together as `*123+`.

---

## ✒️ Developing

-   Secret ideas...

---

## 📝 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgements

-   Original idea from [Quark](https://github.com/VazkiiMods/Quark) by Vazkii.
-   Powered by [Fabric](https://fabricmc.net/) and the awesome Fabric community.

---

## 💬 Contact

-   **Author**: No Namer and Another No Namer
-   **Issues**: Please use the [GitHub issue tracker](https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded/issues) for bug reports and feature requests.

---

*Happy crafting!* 🚀