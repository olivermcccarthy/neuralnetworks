
# neuralnetworks

<div style="background-color:lightblue;">
<div >
<h1> Showing how a simple Neuron can solve a basic quadratic expression.</h1>
<h3> Our neuron is based on our concept of how neurons work in our brains </h3> 
<ul>

<li> Imagine one neuron in your brain that has to decide whether to cross the road or not based on two inputs 
    <ul>
    <li> Input1: Distance of an approaching car.
    <li> Input2: Amount of Money across the road.
    <li> Weight1: This neuron would attach a certain importance to input1.
    <li> Weight2: This neuron would attach a certain importance to input2.
    <li> Bias: It would also be biased towards either safety or money.    
    <li> Equation for Neuron is Weight1*input1 + weight2*input2 + bias.
    <li> Result: > 0 Cross the road . < 0  Do not cross teh road.
    <li> Error: Neuron would learn from this decision. 
    <li> This Neuron may come up with an equation -0.01*Cash + 10*distance -2
    <li> But this equation would be too simple and would result in catastrophe if there was a million euros across the road at a truck was 10 meters away.  
    <li> In reality  millions of neurons would be involved in this one decision to cross the road or not.
    </ul>
</ul>
<h2> Here we only ask our one neuron to solve a simple problem. Our neuron knows nothing about the equation we are trying to solve, except it has two inputs </h2>
<ul>
<li> Equation for Neuron is Weight1*input1 + weight2*input2 + bias.
<li> When our neuron starts up weights and bias are randomly picked. 
<li> For each trial 
<ul> 
<li> We pick random values for X and Y. 
<li> We pass them to the Neuron and get a result (Weight1*input1 + weight2*input2 + bias)
<li> We calculate expected for X and Y using the equation you specified. AX +BY +C 
<li> We calculate  Error as Result - Expected
<li> The neuron adjusts his weights and bias in proportion to the input value and error
<ul>
 <li>weight1 = weight1 -(learningrate *error*input1)
 <li>weight2 = weight2 -(learningrate *error*input2)
 <li>bias = bias -(learningrate *error)
 </ul>
</ul>
<li> The neuron is actually solving the problem using <a href="https://en.wikipedia.org/wiki/Gradient_descent" > Gradient of descent </a> a first-order iterative optimization algorithm for finding the minimum of a function
<li> But our little neuron is unaware of anything as fancy as that. 
<li> Note the speed at which we move closer to expected is determined by learningRate
<li> The theory being that the more trials we run the closer to the expected result we get.      
</ul>
</div> 



### Using Sigmoid to calculate neurons Results

We could use a neuron equation like
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20%20w(1%29*input(1%29%20%2B%20w(2%29*input(2%29%20%2B%20..%20%2B%20w(x%29*input(x%29%20%2B%20...%20%2B%20w(n%29%20%20-%20bias%20  ) 

. But then result would vary too much.
 
 ![d](http://chart.apis.google.com/chart?cht=tx&chl=%20Signoid%20=%20(1%29/(1%20%2B%20e^{-Z}%29%20  ) 

Is used instead  be used to calculate the result of a Neuron. It is used because its value varies between 0 and 1. With the greatest change( most learning occurring around 0.5)

 
###  Variable Definitions
   
| Name | Description |
| --- | --- |
|Sigmoid |        Function to calculate output of neuron  1/(1 + e^-Z) |
| w(x) |      weight a neuron assigns to input(x) 
|Z  |     Sum of    w(1)*input(1) + w(2)*input(2) + .. + w(x)*input(x) + ... + w(n)  - bias 
| bias |  This neuron also has a bias|  
| T   |            Expected value |
|Cost|            Squared difference between expected and actual(Sigmoid)   0.5 *( T - Sigmoid)^2 |
|Cost| If we expect .9 and neuron returns .8 then cost = 0.5( .9 -.8)^2 = 0.05|
|pdW(x)-Cost|     Partial derivative  of weight with respect  to Cost  <br>
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

 pdW(x)-Cost = input(x) *(sigMoid - T) * sigMoid * (1 - sigMoid)


       
### Proof of pdW(x)-Cost = input(x) *(sigMoid - T) * sigMoid * (1 - sigMoid)

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
 
### Proof of pdZ-Sigmoid = sigMoid * (1 - sigMoid)
 
 
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

# 3 Layer Network . How 

How one run works through a 3 layer network works
- We set each neuron in the input layer to a value
- Each neuron in the hidden layer calculates its result based on inputs and weights 
- Each neuron in the output layer calculates its result based on outputs of the hidden and Weights  
- By compared the result with Expected we get An Error (Also known as Cost)
- The goal is to reduce this cost. 
- Using the Error each neuron in the output layers adjusts its weights and bias based on the Partial derivative of  Weight/bias versus Cost. 
- Then each neuron in the hidden Layer adjust its weights and bias. 
- And we repeat the process over and over.  

 
