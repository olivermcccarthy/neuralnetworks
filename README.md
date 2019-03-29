Generated from README.orig by oliver.neuron.readme.CreateFancyFunctions 

# neuralnetworks
Each neuron in our brains learns one thing based on inputs. ( A bit simplistic given we have billions of them)
Imagine a neuron a decides whether to cross the road or not based on
- Amount of money across the road (M)
- Number of cars on the road. (C)

This is a baby neuron and it assigns a random weight to each input
- Wm  weight for amount of money
- Wc weight for number of cars

It then makes a decision to cross the road  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Z%20=%20Wm%20*%20M%20%2B%20Wc%20*C%20  )
 <br>

If Z > 0 we cross the road. 
Then the neuron is told whether it made the right decision or not. 
- -1 dont cross the road
- 1 cross the road
It then adjusts its weights. by an error in proportion to  a learning rate
- Z output of equation 
- expected ( 0 dont cross the road 1 cross the road)  
- error = Z - Expected 
- Learning rate (lr)
- Wm = Wm - error *M * le. ( New money weight  is equal to old money weight - error * money *lr) 
- Wc = Wc - error *C . ( New car weight  is equal to old car weight - error * cars *lr )
as you can see weights are adjusted in proportion to error and value and learning rate

Number of cars can vary from 0 to 10. Money varies from 1 euro to 1000 euros
We normalize these values so they vary from 0 to 1 
- Cars 10 cars normalized to 5 , 4 cars normalized to .4
- Money 100 euros normalized to .1 , 1000 euros normalized to 1, 400 euros normalized to .4
we normalize to reduce the variation in results  
Example
- C = 5 (cars)  0.5
- M = 400 (euros) 0.4
- Wm = 0.24
- Wc = 0.45
- Learning rate (lr) = 0.1  ( learn slowly) 
-  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Z%20=%20Wm%20*%20M%20%2B%20Wc%20*C%20%20Z%20=%200.24%20*%200.4%20%2B%200.45%20*0.5%20=%200.096%20%2B%200.225%20=%200.321%20  )
 <br>  
- Z =   0.321

Expected = -1 dont cross the road
- Error = 0.321 -(-1) = 1.321  
- Wm = Wm - error *M *lr
- Wm = 0.24 -  0.321 *0.4 *.1 = 0.24 - 0.01284 = 0.22716 
- Wc = Wc - error *C *lr
- Wc = 0.45 -  0.321 *.5 *.1 = 0.45 - 0.01605 = 0.43395

So if we tried the equation again
-  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Z%20=%20Wm%20*%20M%20%2B%20Wc%20*C%20%20Z%20=%200.22716%20*%200.4%20%2B%200.43395%20*%200.5%20=%200.090864%20%2B%200.216975%20=%200.307839%20  )
 <br>
- Z = 0.307839 is closer to expected -1.
 
By trying enough random trials the will learn when to cross the road.
The clever among you will realize that the poor neuron will be killed off quickly. But this is maths so our neuron just picks itself up and tries again.
   
It repeats the trial multiple times with varying amounts of money and numbers of cars until it learns the correct weights. 
This could be applied to solving lots of problems, running a trial and adjusting weights based on expected result 


The actual equation used is more complex  than simply multiplying weights by inputs   
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Sigmoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )
 <br>


# Recognizing distorted Letters
Train a network to recognize distorted letters. A distorted letter is drawn on the panel and you tell the network what it is. After a few iterations it gets better and better and recognizing the letter.
  
Equation for each Neuron
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Sigmoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )
 <br>

The network learns each time you click the correct letter. 
The input panel is broken into 10 by 10 squares.  100 input Neurons that are connected to each of the output Neurons  
Weights are shown in a sqaure. Red for positive weights,blue for negative weights  
Watch as the weights change color as the network learns 


# Processing mnist data set


Each Neuron calculates an output based on its inputs and weights using the Sigmoid function.   
This shows how a neural network can be trained to recognize hand written digits.
This jar contains all the training digits from the "http://yann.lecun.com/exdb/mnist/" mnist training set  (60000 plus images).
It knows the expected output for each image. So we use this info to train our network. One pass through the network is referred to as one run.  
 
- This Network has 28*28 inputs Neurons one for each pixel in the input image. Each of these input Neurons is connected to a hidden neuron (These links are not shown). 
- Each hidden neuron is connected to each output Neuron. 
- In neural networks each connection has a weight. Here we cant show all actual weights so we paint a picture of the weights ranging from most positive(Red) to most Negative(Dark Blue). 
- For each trial an input image is loaded and  passed through the network


Each Neuron in the hidden layer calculates its sigmoid based on input*weight. 
Then each Neuron in the output layer calculates its sigmoid based on input(output from hidden Layer)*weight.
 

- Each output Neuron adjusts its weights(A tiny bit) based on this expected value.
- Then each Neuron in the hidden layer adjusts its weights.

And thats just one trial. For the Network to learn with any degree of accuracy we would need to run thousands of runs passing expected value each time so that the network can learn a little each time. 
   
 You can watch as the Network Learns And see it getting better at predicting the digit

# Maths behind it 
This is a very basic look at the maths behind sigmoid and how weights are adjusted
## Neural Networks don't use simple equations. for calculating a neurons output. 
A simple equation would be to multiply all inputs by a weight  
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20Output=%20Z%20=%20w(1%29*input(1%29%20%2B%20w(2%29*input(2%29%20%2B%20..%20%2B%20w(x%29*input(x%29%20%2B%20...%20%2B%20w(n%29%20%20-%20bias%20  )
 <br> 
This could give us a broad range of results bwteen a large negative number and a large positive number 
.Instead of using this output we apply another function to Z.  
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Sigmoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )
 <br>
 where Z is as above . The result of sigmoid varies from 0 to 1. This is nice a neat for us. Im not going to go into all the reasons why this is useful (I dont understand them)  

Sigmoid is used to calculate the result of a Neuron, because its value varies between 0 and 1. Its value changes very little with small changes in weights. With the greatest change( most learning occurring around 0.5). This means we can make tiny improvements in Overall Cost with small changes.
    
##  Variable Definitions
   
| Name | Description |
| --- | --- |
|Sigmoid |        Function to calculate output of neuron  1/(1 + e^-Z) |
| w(x) |      weight a neuron assigns to input(x) 
|Z  |     Sum of    w(1)*input(1) + w(2)*input(2) + .. + w(x)*input(x) + ... + w(n)  - bias 
| bias |  This neuron also has a bias|  
| T   |            Expected value |
|Cost|            Squared difference between expected and actual(Sigmoid)   0.5 *( T - Sigmoid)^2 |
|Cost| If we expect .9 and neuron returns .8 then cost = 0.5( .9 -.8)^2 = 0.05|
|pdW(x)-Cost|     Partial derivative  of weight with respect  to Cost  
|pdW(x)-Z|        Partial derivative  of weight with respect  to Z
|pdZ-Sigmoid|     Partial derivative  of Z with respect  to Sigmoid
|pdSigmoid-Cost|  Partial derivative  of Sigmoid  respect  to Cost
|Error |          Useful variable  pdZ-Sigmoid *  pdSigmoid-Cost

Maths is hard but a small change in each weight causes a small improvement in the cost
So we calculate the partial derivative  of each weight with respect  to Cost
Then adjust this weight by a multiple of this  partial derivative 
Derivative rules here https://www.mathsisfun.com/calculus/derivatives-rules.html





We need to find a small change in each weight that will improve the cost the most
We change them in proportion to pdW(x)-Cost ( Partial Derivative of Weight with respect to Cost) 

There are some funky maths to prove this but for the outputlayer

 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20pdW(x%29-Cost%20=%20input(x%29%20*(sigMoid%20-%20T%29%20*%20sigMoid%20*%20(1%20-%20sigMoid%29%20  )
 <br>n


       
## Proof of pdW(x)-Cost = input(x) *(sigMoid - T) * sigMoid * (1 - sigMoid)

We want to see what improvement in cost a small change in Weight(x) will bring.
   
Using chain rule pdW(x)-Cost =  
 pdW(x)-Sigmoid * pSigmoid-Cost  = 
 pdW(x)-Z  *  pdZ-Sigmoid * pdSigmoid-Cost
 
 As in a small change in weight produces a small change in Z which produces a small change in Sigmoid which reduces the cost.  

|Name|Derivative|Description|
| --- | --- |--- |
| pdW(x)-Z | input(x)|  as we can treat the other coeffs in Z as constants)|
| pdZ-Sigmoid|  Sigmoid*(1 - Sigmoid) |  Proof below| 
| pdSigmoid-Cost|Sigmoid - Expected|
 
## Proof of pdZ-Sigmoid = sigMoid * (1 - sigMoid)
 
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Sigmoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )
 <br>
 Reciprocal Rule  derivative of 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20d({\frac{1}{f}%29%20=%20%20%20-df/(f^{2}%29%20  )
 <br> 
 here    
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20f%20=%20%201%20%2B%20e^{-Z}%20  )
 <br>  and   ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20df%20=%20-e^{-Z}%20  )
 <br> and  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20f^{2}%20=%20(1%20%2B%20e%20^{-Z}%29^{2}%20  )
 <br> 
 
 Thus
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=%20{\frac{e%20^{-Z}%20}{(1%20%2B%20e^{-Z}%29^{2}}%20  )
 <br>
   
 For fun and to get what we are looking for we add and subtract 1 above the line
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20={\frac{1%20%2B%20e%20^{-Z}%20-1}{(1%20%2B%20e^{-Z}%29^{2}}%20  )
 <br> 
 
 Rewriting this we get  
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=%20{\frac{1%20%2B%20e%20^{-Z}}{(1%20%2B%20e^{-Z}%29^{2}}%20-%20{\frac{1}{(1%20%2B%20e^{-Z}%29^{2}}%20  )
 <br> 
 
 Then we take out the following from first and second part 
 
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{1}{(1%20%2B%20e^{-Z}%29}%20  )
 <br> 
 
 Giving us
=
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=({\frac{1}{(1%20%2B%20e^{-Z}%29}%29(1%20-{\frac{1}{(1%20%2B%20e^{-Z}%29}%29%20%20  )
 <br>
  

  
Of course  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{1}{(1%20%2B%20e^{-Z}%29}%20  )
 <br> Is Sigmoid 
  Therefore the partial derivative of Sigmoid with respect to Z is  
  
  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20{\frac{\partial%20Sigmoid}{\partial%20Z}%20=%20Sigmoid*(1%20-%20Sigmoid%29%20  )
 <br>
  
  This is useful because we can easily calculate the derivative of Sigmoid
 
