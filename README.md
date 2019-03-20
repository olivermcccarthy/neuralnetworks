
# neuralnetworks

Neural Networks don't use simple equations. for calculating a neurons output. 
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20Output=%20Z%20=%20w(1%29*input(1%29%20%2B%20w(2%29*input(2%29%20%2B%20..%20%2B%20w(x%29*input(x%29%20%2B%20...%20%2B%20w(n%29%20%20-%20bias%20  ) 

.Instead they use complex Ones like 
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Signoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )
 where Z is as above 

Sigmoid is used to calculate the result of a Neuron, because its value varies between 0 and 1. Its value changes very little with small changes in weights. With the greatest change( most learning occurring around 0.5). This means we can make tiny improvements in Overall Cost with small changes.
 
# Recognizing wonky Letters
Train a network to recognize wonky letters. A wonky letter is drawn on the panel and you tell the network what it is. After a few iterations it gets better and better and recognizing the letter.
  
Equation for each Neuron
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Signoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  )

The network learns each time you click the correct letter. 
The input panel is broken into 10 by 10 squares.  100 input Neurons that are connected to each of the output Neurons  
Weights are shown in a sqaure. Red for positive weights,blue for negative weights  
Watch as the weights change color as the network learns 


# Processing many hand written digits


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

![d](http://chart.apis.google.com/chart?cht=tx&chl=%20pdW(x%29-Cost%20=%20input(x%29%20*(sigMoid%20-%20T%29%20*%20sigMoid%20*%20(1%20-%20sigMoid%29%20  )n


       
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
 Reciprocal Rule  derivative of 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20d({\frac{1}{f}%29%20=%20%20%20-df/(f^{2}%29%20  ) 
 here    
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20f%20=%20%201%20%2B%20e^{-Z}%20  )  and  ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20df%20=%20-e^{-Z}%20  ) and ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20f^{2}%20=%20(1%20%2B%20e%20^{-Z}%29^{2}%20  ) 
 
 Thus
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=%20{\frac{e%20^{-Z}%20}{(1%20%2B%20e^{-Z}%29^{2}}%20  )
   
 For fun and to get what we are looking for we add and subtract 1 above the line
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20={\frac{1%20%2B%20e%20^{-Z}%20-1}{(1%20%2B%20e^{-Z}%29^{2}}%20  ) 
 
 Rewriting this we get  
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=%20{\frac{1%20%2B%20e%20^{-Z}}{(1%20%2B%20e^{-Z}%29^{2}}%20-%20{\frac{1}{(1%20%2B%20e^{-Z}%29^{2}}%20  ) 
 
 Then we take out the following from first and second part 
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{1}{(1%20%2B%20e^{-Z}%29}%20  ) 
 
 Giving us
=
![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{-df}{f^{2}}%20=({\frac{1}{(1%20%2B%20e^{-Z}%29}%29(1%20-{\frac{1}{(1%20%2B%20e^{-Z}%29}%29%20%20  )
  

  
Of course ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20{\frac{1}{(1%20%2B%20e^{-Z}%29}%20  ) Is Sigmoid 
  Therefore the partial derivative of Sigmoid with respect to Z is  
  
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20{\frac{\partial%20Sigmoid}{\partial%20Z}%20=%20Sigmoid*(1%20-%20Sigmoid%29%20  )
  
  This is useful because we can easily calculate the derivative of Sigmoid
 
