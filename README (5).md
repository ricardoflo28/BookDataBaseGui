# BookDatabaseGUI (Binary Tree + JavaFX)

A small desktop app that stores books in a **self-balancing binary tree (AVL tree)**. You can add, remove and find books, save and load the database to a file, and view the tree as a picture. The interface is built with JavaFX.

## How to run

1. Install JDK 21 or newer and download the [JavaFX SDK](https://gluonhq.com/products/javafx/).
2. Open the project folder in VS Code (it must contain `src`, `books` and `.vscode`).
3. In `.vscode/settings.json` and `.vscode/launch.json`, set the JavaFX path to your SDK's `lib` folder.
4. Press F5 and choose `DataBaseGui`.

## Project layout

```
BookDatabaseGUI/
├── src/      Java source files
├── books/    Cover pictures (png, jpg, gif or bmp - JavaFX can't load webp)
└── .vscode/  VS Code settings (JavaFX path and launch config)
```

## How each class works

### `Books`
Represents one book with a **title**, **author** and **year**.
- Implements `Serializable`, so books can be saved to a file.
- Implements `Comparable<Books>`: books are compared by title, ignoring case. This is what decides where a book goes in the tree.
- `toString()` returns text like `The Martian by Andy Weir (2014)`.
- Getters: `getTitle()`, `getAuthor()`, `getYear()`.

### `BinaryTreeNode<E>`
One node (box) in the tree. It holds:
- `data`: the value stored in the node (a `Books` object here)
- `left` and `right`: the child nodes (or `null`)
- `height`: how tall the subtree under this node is, used for balancing

### `BinaryTree<E>`
The data structure itself. It is a generic **AVL tree**: after every insert or remove, it checks whether the tree has become lopsided and fixes it with rotations.
- `addData(E)`: inserts a value. Smaller values go left and larger go right. Duplicates are ignored. Then it updates heights and rebalances.
- `remove(E)`: removes a value. If the node has two children, it is replaced by the smallest value on its right side. Then it rebalances.
- `find(E)`: searches the tree and returns the matching item, or `null`.
- `leftRotation` / `rightRotation`: the rotations that keep the tree balanced (single and double rotations are built from these).
- `toString()`: the contents in sorted (in-order) order, indented by depth.
- `toList()`: the contents as a sorted `List`. The GUI uses this to show the book cards.
- `printTree()`: a text drawing of the tree's shape.
- `getRoot()`: gives the root node so the GUI can draw the tree diagram.

### `DataBaseAccess`
The layer between the GUI and the tree, so the GUI never touches the tree directly.
- Creates the tree and loads `database.dat` automatically when the app starts, if the file exists.
- `addBook`, `remove`, `find`: wrappers around the tree methods.
- `getAllBooks()`, `treeToString()`, `getTreeDiagram()`, `getRoot()`: different views of the tree's contents.
- `saveDataBase()`: writes the whole tree to `database.dat` using Java serialization.
- `loadDataBase()`: reads the tree back from `database.dat`.

### `DataBaseGui`
The JavaFX window (extends `Application`).
- **Dropdown:** pick one of the pre-defined books, with a small cover next to each title.
- **Buttons:** Add, Remove, Find Book, Save, Load, and Show Diagram / Show Books.
- **Book list:** each book is a card with its cover, title, author and year, in sorted order.
- **Tree diagram:** draws each node as a card connected to its children by curved lines, with the root at the top.
- **Status bar:** shows messages such as "Added: The Martian" or "Book not found".
- **Covers:** loaded from the `books` folder. If a picture is missing, a colored placeholder is drawn instead.
- `main` calls `launch(args)` to start the app.

## How the pieces fit together

```
DataBaseGui  ->  DataBaseAccess  ->  BinaryTree<Books>  ->  BinaryTreeNode<Books>
 (window)        (database API)      (AVL logic)             (stores a Books object)
```

## Saved data

Pressing **Save** writes `database.dat` in the project folder. **Load** reads it back. If you change the `Books` or `BinaryTree` classes, an old `database.dat` may stop loading, so delete it and save again.
