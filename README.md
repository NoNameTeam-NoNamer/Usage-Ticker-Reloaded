# Usage Ticker Reloaded

![Mod Version](https://img.shields.io/badge/version-1.0.0-blue) ![Minecraft](https://img.shields.io/badge/Minecraft-26.1+-green) ![Fabric](https://img.shields.io/badge/Fabric-0.19.3+-orange) ![Side](https://img.shields.io/badge/Client-side-yellow)

**Usage Ticker Reloaded** is a lightweight client-side Fabric mod that replicates the popular “Usage Ticker” feature from the Quark mod for modern Minecraft versions (≥26.1). Since [the original Usage Ticker mod](https://modrinth.com/mod/usage-ticker) hasn't been updated for a long time, I made this similar mod.It displays the icon and total count of the item you're holding, besides your hotbar.

> **Note:** This mod is **client-side only**. It does nothing when installed on a dedicated server.

---

## ✨ Features

-   📦 **Real‑time item count** – Shows the total number of the currently held item (including all stacks in your inventory).
-   👆 **Dual‑hand support** – Both main hand and offhand items are displayed simultaneously (if different).
-   🧹 **Smart deduplication** – If both hands hold the same item, it only shows once with the combined total.
-   🪣 **No world changes** – Adds no blocks, items, recipes, or gameplay mechanics – purely informational.
-   🎨 **Clean HUD integration** – Renders alongside vanilla elements, respecting your GUI scale.

---

## 📥 Installation

1.  **Install Fabric Loader** for Minecraft.
2.  **Download the mod JAR** from the [Releases](https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded/releases) page.
3.  Place the JAR in your `mods` folder.
4.  Launch the game and enjoy!

*Requires [Fabric API](https://modrinth.com/mod/fabric-api)*

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

## 🖼️ Screenshots

Will be shown in the future.

---

## 📋 Compatibility

| Minecraft       | Fabric Loader | Status                                                                      |
|:----------------|:--------------|:----------------------------------------------------------------------------|
| 26.3 SNAPSHOTS  | 0.19.3+       | ❔ Maybe work                                                               |
| 26.2            | 0.19.3+       | ✅ Fully tested                                                             |
| 26.1            | 0.19.3+       | ✅ Fully tested                                                             |
| 1.21.1 or below | -             | ⚠️ You should use [the original mod](https://modrinth.com/mod/usage-ticker) |
---

## ❓ FAQ

**Q: Will you support versions below 26.1 or other loaders?**  
A: I won't, since I don't have time. However, if you have the ability to do that, feel free to [submit a pull request](https://github.com/NoNameTeam-NoNamer/Usage-Ticker-Reloaded/pulls).

**Q: Can I change the position/color of the counter?**  
A: Currently you need to modify the source constants (see above). A configuration file is planned for a future release.

**Q: Does it count items inside shulker boxes and bundles?**  
A: Not yet, but support for that (with a config toggle) is on the roadmap.

---

## ✒️ Developing

-   Config support
-   Shulker box and bundle count support
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