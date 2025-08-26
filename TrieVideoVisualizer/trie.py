class TrieNode:
    """A node in the Trie structure."""
    def __init__(self):
        self.children = {}
        self.is_end_of_word = False

class Trie:
    """A Trie data structure."""
    def __init__(self):
        self.root = TrieNode()

    def insert(self, word: str):
        """Inserts a word into the trie."""
        current_node = self.root
        for char in word:
            if char not in current_node.children:
                current_node.children[char] = TrieNode()
            current_node = current_node.children[char]
        current_node.is_end_of_word = True