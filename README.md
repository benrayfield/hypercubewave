# hypercubewave
Superpositions 1 pixel many times, becomes 2 pixels, 4, 8, and so on, until it looks like a wave with nonlinear curves caused by Subset Sum of Npcomplete. Opensource GNU GPL 2+. Requires java 8+.

Its a 2d grid where brightness of each cell equals, the sum for all paths, of a constant exponent set size for all subsets of the 2d vectors (a set of such vectors is chosen with the keyboard and mouse) which sum to that 2d postion from the 1 pixel it all starts at. That constant is -1/sqrt(2) by default. Then absolute value. The brightness is linearly scaled between the min (0) and max of those numbers. If you dont absval, you get gray and half of them are symmetricly dark.

<nobr>
  <img src="https://raw.githubusercontent.com/benrayfield/hypercubewave/master/pics/HypercubeWave_0.1_pic3.png"/>
  <img src="https://raw.githubusercontent.com/benrayfield/hypercubewave/master/pics/HypercubeWave_0.1_pic2.png"/>
  <img src="https://raw.githubusercontent.com/benrayfield/hypercubewave/master/pics/HypercubeWave_0.1_pic1.png"/>
</nobr>
