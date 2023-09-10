# 16-bit TTRPG Game Map Level Editor
-------------------
This is a map editor for my game.

Each map is in a scene. There are several types of scene: battle, narrative, cutscene.

Each map is rectangular and has a width and a height. It is divided into tiles. Each tile has textured triangles on it.

The map is isometric.

## UI Features
 * Toolbar : Contextual based on if editor is showing the scene view, the map view, the tile view, or the texture editor view.
 ** Global Toobar : Save, Load, New, Exit, Export
 ** Scene Toolbar : Palette (Open Palette editor), 
 ** Map Toolbar :
 ** Tile Toolbar :
 ** Texture Toolbar :
 
 **
 * Editor : A window that holds several views as well as the view heirachy for easy navigation.
 ** Scene editor : Show some stuff about scene
    ** Color Palette : 15 color palette
    ** Shape Palette : These are the shapes availble for tiles. A Shape is a set of co-plainar triangles.
    ** Object Palette : These are the objects made from shapes available in the map
 ** Map editor : Can place tiles 
 ** Tile editor : Show the mesh, place objects and assign texture and palettes.
 ** Texture editor : Edit the texture bitmap. Use colors from scene palette
 ** Palette editor : allows user to edit palettes


## Scene file Format
 * TODO * This section describes the human readable file format used by the editor to save/reload.
## Scene file binary format
 * TODO * This section describes the binary that will be consumed by the game.

Summers