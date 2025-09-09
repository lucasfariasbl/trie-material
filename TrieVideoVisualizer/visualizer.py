import os
import shutil
import re
from graphviz import Digraph
from trie import Trie, TrieNode

class Visualizer:
    def __init__(self, output_dir="output"):
        self.output_dir = output_dir
        self.frame_count = 0
        self.graph_size = None # To be determined in the first pass

        if os.path.exists(self.output_dir):
            shutil.rmtree(self.output_dir)
        os.makedirs(self.output_dir)

    def _get_graph_size(self, trie: Trie):
        """Pass 1: Build the full graph to determine its final size."""
        dot = Digraph()
        dot.attr(rankdir='TB', splines='curved')
        dot.node("root", "Trie", shape="plaintext", fontsize="30", fontname="Helvetica-Bold")
        dot.edge("root", str(id(trie.root)), style="dashed")
        self._add_nodes_edges(dot, trie.root)
        
        # Render to dot source and parse the bounding box
        dot_source = dot.pipe(format='dot').decode('utf-8')
        bb_match = re.search(r'bb="([\d\.,]+)"', dot_source)
        if bb_match:
            # bb is in points (1/72 inch), format is "llx,lly,urx,ury"
            _, _, urx, ury = map(float, bb_match.group(1).split(','))
            # Convert to inches for the size attribute
            self.graph_size = f"{urx / 72},{ury / 72}"

    def _add_nodes_edges(self, dot: Digraph, node: TrieNode, parent_id: str = None, char: str = None, traversed_node_id: str = None, new_node_id: str = None):
        node_id = str(id(node))
        node_label = char if char else ""

        # Node styling
        if node_id == traversed_node_id:
            dot.node(node_id, label=node_label, shape="doublecircle", color="blue", style="filled", fillcolor="#f1c40f", penwidth="3", fontname="Helvetica", fontsize="24")
        elif node_id == new_node_id:
            dot.node(node_id, label=node_label, shape="circle", color="green", style="filled", fillcolor="#2ecc71", fontname="Helvetica", fontsize="24")
        else:
            shape = "doublecircle" if node.is_end_of_word else "circle"
            fillcolor = "#bdc3c7" if node.is_end_of_word else "#ecf0f1"
            dot.node(node_id, label=node_label, shape=shape, color="black", style="filled", fillcolor=fillcolor, fontname="Helvetica", fontsize="24")

        if parent_id:
            dot.edge(parent_id, node_id, label="")

        for char, child_node in node.children.items():
            self._add_nodes_edges(dot, child_node, node_id, char, traversed_node_id, new_node_id)

    def _capture_frame(self, trie_root: TrieNode, traversed_node_id: str = None, new_node_id: str = None):
        dot = Digraph()
        dot.attr(rankdir='TB', splines='curved')
        dot.attr('graph', dpi='300')
        dot.node("root", "Trie", shape="plaintext", fontsize="30", fontname="Helvetica-Bold")
        dot.edge("root", str(id(trie_root)), style="dashed")
        
        self._add_nodes_edges(dot, trie_root, traversed_node_id=traversed_node_id, new_node_id=new_node_id)
        
        filename = f"frame_{self.frame_count:04d}"
        filepath = os.path.join(self.output_dir, filename)
        dot.render(filepath, format='png', cleanup=True)
        self.frame_count += 1

    def generate_visualization(self, words: list):
        """Pass 2: Generate frames for the video."""
        vis_trie = Trie()
        self._capture_frame(vis_trie.root) # Initial empty frame

        for word in words:
            print(f"Visualizing insertion of '{word}'...")
            current_node = vis_trie.root
            for char in word:
                self._capture_frame(vis_trie.root, traversed_node_id=str(id(current_node)))
                if char not in current_node.children:
                    new_node = TrieNode()
                    current_node.children[char] = new_node
                    self._capture_frame(vis_trie.root, traversed_node_id=str(id(current_node)), new_node_id=str(id(new_node)))
                current_node = current_node.children[char]
            
            current_node.is_end_of_word = True
            self._capture_frame(vis_trie.root, traversed_node_id=str(id(current_node)))
        
        self._capture_frame(vis_trie.root) # Final frame

    def create_video(self, words: list, output_filename="trie_visualization_final.mp4", framerate=2):
        # Pass 1: Get final graph size
        print("Pass 1: Calculating final graph layout...")
        final_trie = Trie()
        for word in words:
            final_trie.insert(word)
        self._get_graph_size(final_trie)

        # Pass 2: Generate frames
        print("\nPass 2: Generating frames...")
        self.generate_visualization(words)

        # Compile video
        video_path = os.path.join(self.output_dir, output_filename)
        frames_pattern = os.path.join(self.output_dir, "frame_%04d.png")

        print("\nCompiling video...")

        # Determine canvas size from graphviz output to create a static frame.
        w_inch, h_inch = map(float, self.graph_size.split(','))
        dpi = 300  # Match DPI from _capture_frame
        max_w = int(w_inch * dpi) + 100
        max_h = int(h_inch * dpi) + 100

        # Ensure dimensions are even for the yuv420p codec.
        max_w = (max_w + 1) // 2 * 2
        max_h = (max_h + 1) // 2 * 2

        # Create a video with a static canvas.
        # The 'pad' filter creates a canvas of a fixed size (max_w x max_h) with a white background.
        # x=-1:y=-1 centers the input frame on the canvas.
        # This ensures all frames are placed on the same background, creating a "static camera" effect.
        command = (
            f"ffmpeg -y -framerate {framerate} -i '{frames_pattern}' "
            f"-vf \"pad={max_w}:{max_h}:-1:-1:color=white\" "
            f"-c:v libx264 -crf 18 -r {framerate} -pix_fmt yuv420p '{video_path}'"
        )

        os.system(command)
        print(f"Video saved to {video_path}")
