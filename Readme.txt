                                                                     
                                                                     
                                                                     
                                             
						Welcome to SPACE PACKER!

Introduction:
	
	Space packer is a game about moving a box to a goal location. You accomplish this by programming the robot to move around the level and push the block.

Commands:
 Forward:
	Moves the player one block forward
 Left:
	Turns the robot to the left
 Right:
	Turns the robot to the right
 Push:
	If there is a block in front of the robot, it pushes it and moves forward
 Anti-Gravity:
	Flips the robot's gravity
 F1:
	Executes the functions in the F1 section
 F2:
	Executes the functions in the F2 section

Mechanics:
 Winning the game:
	To win the game you must move a large blue box onto the smaller spinning box that designates the goal
 Types of walls:
	Concrete: This is the standard wall
	Metal: If you are directly facing this kind of wall and move forward, you will move onto it. You have magnetic wheels(Don't question it)
 The Box: 
	Upon pushing the cube your gravity is applied to it (Even if it's because you are held to the wall with your wheels)
 Losing:
	Your program fails if you or a box hits the edge of the field.

Making a Level:
	You can make your own levels!
	In the game directory there is a folder called levels, if you place your levels inside, the game will detect and place them on the level 		select screen.
	Level file format:
		A level is a 10x10x10 cube. This corresponds to a 3 dimensional array that represents the location of objects in the room.
		

		To place a Wall (a solid block) in the room you place the character 'W' as the first character of the line.
		 This is followed by the coordinates of the block, then the surface type of each side.
		  Example:
		   W 4 5 5 0 0 0 0 0 0
		 The six numbers after the coordinates correspond to the -x,x,-y,y,-z,z sides of the block.
		

		To place a box, you start the line with a 'B' followed by the coordinates of the box.
		The next three numbers correspond to the direction of gravity(x,y,z). A one for positive on that axis negative for negative on 				 that axis
		  Example:
		   B 3 4 5 0 -1 0
		
		To create a player you start the line with 'P' followed by the coordinates
		 Example:
		  P 2 4 5
		
		To create a goal you start the line with 'G' followed  by the coordinates
		 Example: 
		  G 1 2 3

		To limit the number of commands you are allowed in each function, start the line with 'L' followed by the number of commands the 			 player is ALLOWED for main,F1,and F2 respectively
		  Example:
		   L 16 16 16
		This number must be greater than 0 and less than or equal to 16