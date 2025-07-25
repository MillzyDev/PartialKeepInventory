# PartialKeepInventory
Frustrated when you lose all your important stuff and suddenly your world/server grinds to a halt? Hate how consequence free keepInventory feels? PartialKeepInventory aims to find a "happy middleground" between the two.

PartialKeepInventory is a highly modular alternative to using keepInventory; you can keep specific groups of items, certain slots and even individual items from being dropped upon death, as opposed to your whole inventory and levels.

## Usage
To start with, ensure that `keepInventory` is **disabled** and instead enabled the `partialKeepInventory` gamerule.

![](https://raw.githubusercontent.com/MillzyDev/PartialKeepInventory/refs/heads/main/assets/enablepki.gif)

### Enabling Features

Currently, nothing is set up and dying will behave as normal with `keepInventory` disabled. You need to enable "features" individually. For this example, we want to keep my tools and equipped armour when we die. So we enable the `tools` and `equipment` features, using the `partialKeepInventory features add` command. 

You can also check what features are enabled using the `partialKeepInventory features` command.

![](https://raw.githubusercontent.com/MillzyDev/PartialKeepInventory/refs/heads/main/assets/addpkifeatures.gif)

The complete list of features is as follows:
* `equipment` - Any items equipped in your armor slots
* `equippables` - Any items that are able to be equipped in your amor slots.
* `tools` - Any items with a tool component, such as pickaxes and shovels (should also include weapons)
* `hotbar` - Any items that are in your hotbar.
* `offhand` - Any items in your offhand.
* `epic/rare/common/uncommon` - Any items with the respective rarity, indicated by name colour (purple/blue/yellow/white).
* `experience` - Experience and levels
* `customList` - Any items added to the `itemList` using the `partialKeepInventory itemList add` command.