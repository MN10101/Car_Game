
# Car Game

A simple car game implemented in Java using Swing. The game features two cars driving on a road, with sound effects for the road, car horns, and collisions. The game automatically increases the speed as the player progresses.

## Features

- Two lanes with cars that the player can move between.
- Road sound effect playing continuously.
- Car horn sound effect when changing lanes.
- Collision detection with appropriate sound effects for brakes and collision.
- Automatic speed increase as the player progresses.

## Requirements

- Java 8 or higher

## Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/cargame.git
   cd cargame
   ```

2. **Add the required sound files:**

   Place the following WAV files in the `src` directory:
   - `road.wav` (continuous road noise)
   - `horn.wav` (car horn sound)
   - `brake.wav` (brake sound when collision happens)
   - `collision.wav` (collision sound when collision happens)

3. **Compile and run the game:**

   Using your favorite IDE or the command line, compile and run the game:

   **Using the command line:**
   ```bash
   javac -d out -sourcepath src src/CarGame.java
   java -cp out CarGame
   ```

## Controls

- **Left Arrow / A:** Move the car to the left lane.
- **Right Arrow / D:** Move the car to the right lane.

## Code Explanation

The game consists of a single class `CarGame` which extends `JPanel` and implements `ActionListener` and `KeyListener`. The main features are:

- **Initialization:**
  - Loads car images and sound files.
  - Sets initial positions for the cars and initializes the game timer.

- **Paint Component:**
  - Draws the road, cars, and game information on the screen.

- **Key Listener:**
  - Moves the player's car left or right based on the key pressed and plays the horn sound.

- **Action Listener:**
  - Updates the position of the opponent car and checks for collisions.
  - Increases the speed at regular intervals.

## Sound Loading

The sound files are loaded using the `javax.sound.sampled` package:

```java
private Clip loadSound(String filePath) {
    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        return clip;
    } catch (Exception e) {
        System.err.println("Error loading sound: " + filePath);
        e.printStackTrace();
        return null;
    }
}
```

### Playing Sound

The sounds are played using the following method:

```java
private void playSound(Clip clip) {
    if (clip != null) {
        clip.setFramePosition(0);  // Rewind to the beginning
        clip.start();
    }
}
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contributing

1. Fork the repository
2. Create a new branch (`git checkout -b feature-foo`)
3. Commit your changes (`git commit -am 'Add some foo'`)
4. Push to the branch (`git push origin feature-foo`)
5. Create a new Pull Request

## Acknowledgements

- Sound effects from [freesound.org](https://freesound.org/).
- Images used for cars.

Enjoy the game!
