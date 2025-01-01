# Computer-Graphics

This repository contains projects exploring the fascinating world of computer graphics. The focus is on implementing both 2D and 3D graphical applications, showcasing various techniques and concepts used in the field.

---

## Projects Overview

### 1. 2D Graphics with JavaFX
- A project demonstrating 2D graphics using the JavaFX framework.
- Features include:
  - Custom shapes and animations.
  - User interactions and event handling.
  - Rendering optimization for smooth performance.

### 2. 3D Cube Project (OpenGL)
- A project showcasing a rotating 3D cube implemented with **OpenGL**.
- Features include:
  - **Real-time rendering**: A fully interactive 3D cube.
  - **Custom shaders**: Written in GLSL for rendering effects.
  - **Keyboard controls**: Move the cube in 3D space.
  - **Depth testing**: Proper rendering of cube faces using depth buffers.

---

## Getting Started

### Prerequisites
- **2D Project**:
  - Requires Java 11 or later and JavaFX SDK.
- **3D Cube Project**:
  - Requires:
    - OpenGL (Version 3.3 or higher).
    - Development tools such as Visual Studio (or similar).
    - Libraries: GLFW, GLAD, stb_image, and glm.

### Installation and Testing (3D Cube)

1. **Download the Project**:
   - Clone the repository:
     ```bash
     git clone https://github.com/DespotS/Computer-Graphics.git
     ```
   - Or download the provided `.zip` file and extract it.

2. **Open the Project**:
   - Open the `3dcube.sln` file in Visual Studio (or your preferred IDE).

3. **Build and Run**:
   - Ensure the required libraries (GLFW, GLAD, stb_image, glm) are included in the project.
   - Build the solution using the `Debug` or `Release` configuration.
   - Run the executable (`3dcube.exe`) located in `3dCube/x64/Debug/`.

4. **Controls**:
   - **W**: Move the cube upward.
   - **S**: Move the cube downward.
   - **A**: Move the cube left.
   - **D**: Move the cube right.
   - **Q**: Zoom in.
   - **E**: Zoom out.
   - **ESC**: Exit the program.


## Known Issues
- Large files have been excluded from the repository due to size constraints. Ensure dependencies such as `glfw3.dll` are correctly placed in the project directory.
- Ensure proper driver support for OpenGL 3.3 or higher.



