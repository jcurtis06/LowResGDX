# LowResGDX
How I set up low-res/pixel-perfect rendering in libGDX

### Features
- Resizes to any screen size (adds black bars depending on aspect ratio)
- Rotation, scaling, etc. are all pixel perfect (see images below)

### How it works
- First, the game renders all the sprites to an `FrameBuffer` at a set resolution
- The `FrameBuffer` is then scaled to the screen size
- There are two cameras, one that only looks at the `FrameBuffer` (called `fboCamera`)
- The other camera (simply called `camera`), is the one that actually follows the player

### Before (_Very smooth rotation_)
<img src="https://github.com/jcurtis06/LowResGDX/assets/77545656/d92d009f-b3e9-4c02-8689-299ae323d050" alt="bad" width="500" height="500">

### After (_Pixelated rotation_)
<img src="https://github.com/jcurtis06/LowResGDX/assets/77545656/f252de3b-7d37-40d9-a704-958cb3b8732a" alt="image" width="716" height="448">
