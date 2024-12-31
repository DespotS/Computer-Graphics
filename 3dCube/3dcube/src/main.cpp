#include<iostream>
#include<stb/stb_image.h>
#include<glad/glad.h>
#include<GLFW/glfw3.h>
#include<fstream>
#include<streambuf>
#include<sstream>
#include<string>

#include<glm/glm.hpp>
#include<glm/gtc/type_ptr.hpp>
#include<glm/gtc/matrix_transform.hpp>

#include "Shader.h"




void framebuffer_size_callback(GLFWwindow* window, int width, int height);

//method to process input
void processInput(GLFWwindow* window, glm::vec3& position);



int main() {
    std::cout << "Welcome and enjoy my 3D Cube" << std::endl;
    std::cout << "Press ESC to exit ." << std::endl;
    std::cout << "Press Q for closeup ." << std::endl;
    std::cout << "Press E for zoom out ." << std::endl;
    std::cout << "Press D to move right ." << std::endl;
    std::cout << "Press A to move left ." << std::endl;
    std::cout << "Press W to move up ." << std::endl;
    std::cout << "Press S to move down ." << std::endl;

    glfwInit();
    

    //openGL version 3.3
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

# ifdef __APPLE__
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
#endif

    //create the window object

    GLFWwindow* window = glfwCreateWindow(900, 500, "LearnOpenGL", NULL, NULL);
    if (window == NULL)
    {
        std::cout << "Failed to create GLFW window" << std::endl;
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);



    //manage gl functions
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
    {
        std::cout << "Failed to initialize GLAD" << std::endl;
        return -1;
    }

    glViewport(0, 0, 700, 500);

    glEnable(GL_DEPTH_TEST);

    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);

    Shader shader("assets/vertex_core.glsl", "assets/fragment_core.glsl");

    //vertex array these are just bytes (square, 2 triangles)
    float vertices[] = {
        // position            colors
    -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f,  // Bottom-left (Red)
     0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 0.0f,  // Bottom-right (Green)
     0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f,  // Top-right (Blue)
    -0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f,  // Top-left (Yellow)

    // Back face
    -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f,  // Bottom-left (Magenta)
     0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 1.0f,  // Bottom-right (Cyan)
     0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1.0f,  // Top-right (White)
    -0.5f,  0.5f, -0.5f,  0.2f, 0.8f, 0.5f   // Top-left (Pastel green)

    };

    unsigned int indices[] = { 
        // Front face
           0, 1, 2,
           2, 3, 0,

           // Back face
           4, 5, 6,
           6, 7, 4,

           // Left face
           4, 0, 3,
           3, 7, 4,

           // Right face
           1, 5, 6,
           6, 2, 1,

           // Top face
           3, 2, 6,
           6, 7, 3,

           // Bottom face
           4, 5, 1,
           1, 0, 4
    };

    //VAO, VBO
    unsigned int VAO, VBO, EBO;
    glGenVertexArrays(1, &VAO);
    glGenBuffers(1, &VBO);
    glGenBuffers(1, &EBO);

    //bind VAO
    glBindVertexArray(VAO);

    //bind the VBO
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    //set EBO
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);
   

    //set attribute pointer 
   
    // positions
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);
    //colors
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    
    
    //glm::mat4 trans = glm::mat4(1.0f);
    glm::mat4 projection = glm::perspective(glm::radians(45.0f), 900.0f / 500.0f, 0.1f, 100.0f);
    //glm::mat4 view = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.0f, -3.0f));
    //glm::mat4 model = glm::mat4(1.0f);
   // model = glm::rotate(model, (float)glfwGetTime(), glm::vec3(0.5f, 1.0f, 0.0f));
    glm::vec3 position(0.0f, 0.0f, -3.0f);

    shader.activate();

    shader.setMat4("projection", projection);
    //shader.setMat4("view", view);
    //shader.setMat4("transform", model);


    //main while loop
    while (!glfwWindowShouldClose(window))
    {
        //process input
        processInput(window, position);

        //render 
        glClearColor(0.1f, 0.7f, 0.6f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        glm::mat4 view = glm::translate(glm::mat4(1.0f), position);
        glm::mat4 model = glm::rotate(glm::mat4(1.0f), (float)glfwGetTime(), glm::vec3(0.5f, 1.0f, 0.0f));
        
        shader.activate();
        shader.setMat4("view", view);
        shader.setMat4("transform", model);

        glBindVertexArray(VAO);
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);


        //send new frame to window
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    glDeleteVertexArrays(1, &VAO);
    glDeleteBuffers(1, &VBO);
    glDeleteBuffers(1, &EBO);


    glfwTerminate();


    return 0;
}

//resize the window
void framebuffer_size_callback(GLFWwindow* window, int width, int height)
{
    glViewport(0, 0, width, height);
}

void processInput(GLFWwindow* window,glm::vec3 &position)
{
    float speed = 0.05f;

    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) 
        glfwSetWindowShouldClose(window, true);//exit

    if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS)
        position.z += speed; //closeup
    if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS)
        position.z -= speed; //back
    if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
        position.x += speed; //move right
    if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
        position.x -= speed; //move left
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
        position.y += speed; //move up
    if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
        position.y -= speed; //move down

    
}

