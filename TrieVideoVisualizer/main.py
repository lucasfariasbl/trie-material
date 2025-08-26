from visualizer import Visualizer

def main():
    # 1. Define the words to be inserted into the Trie
    words_to_insert = [
    "adson",
    "adriano",
    "adeus",
    "lucas",
    "luz",
    "lua",
    "lar"
    ]

    # 2. Create a Visualizer instance
    visualizer = Visualizer(output_dir="output")

    # 3. Generate the video
    # This single method now handles calculating the layout, generating all the frames, and compiling the video.
    visualizer.create_video(words_to_insert, framerate=2)

if __name__ == "__main__":
    main()
