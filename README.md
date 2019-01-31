# neuralnetworks

Taking a full adder as an example we use a network of 3 Layers
Fairly simple but it shows how our neural network goes through all its math and comes up with a solution of its own

| Layer | Description | 
| --- | --- |
| input | 3 input neurons (in1 , in2, carryin)|
| hidden | 6 neurons we could use as few or as many as we need|
| output| 2 neurons sum and carryout|  

Truth table for full adder

| in1 | in2 | cin| sum| cout|
| --- | --- |--- | --- | --- |
|0|0|0|0|0
|0|0|1|1|0
|0|1|0|1|0
|0|1|1|0|1
|1|0|0|1|0
|1|0|1|0|1
|1|1|0|0|1
|1|1|1|1|1   

![alt text](adder.bmp "Logo Title Text 1")


- We randomly come up with weights ( a number between 0 and 1) 
- Pass through the first row 
- Get the output
- Pass back the error
- Adjust each weight by a small bit ( Get a small bit closer to the solution)
- Pass the next row and so on.
- Keep going until the result matches the expected for each row in the truth table.
 
###Some variable definitions

| Name | Description |
| --- | --- |
|Sigmoid |        Function to calculate output of neuron  1/(1 + e^-Z) |
| w(x) |      weight a neuron assigns to input(x) 
|Z  |             Sum of all weights * inputs  w(1)*input(1) + w(2)*input(2) + .. + w(x)*input(x) + ... + w(n)  - bias 
| bias |  This neuron also has a bias

- We set each neuron in the input layer to its value
- Each neuron in the hidden layer calculates its result using Sigmoid
- Each neuron in the output layer calculates its result using Sigmoid 
- We pass the expected values to the output layer
- Using the expected values each neuron in teh output layers adjusts 

Adjust each weight by an errorVariable
Maths is hard but a small change in each weight causes a small improvement in the cost
So we calculate the partial derivative  of each weight with respect  to Cost
Then adjust this weight by a multiple of this  partial derivative 
Derivative rules here https://www.mathsisfun.com/calculus/derivatives-rules.html
Example 


###Some variable definitions

| Name | Description |
| --- | --- |
| T   |            Expected value |
|Sigmoid |        Function to calculate output of neuron  1/(1 + e^-Z) |
|Z  |             Sum of all weights * inputs  w(1)*input(1) + w(2)*input(2) + .. + w(x)*input(x) + ... + w(n)*input(n)| 
|Cost|            Squared difference between expected and actual(Sigmoid)   0.5 *( T - Sigmoid)^2 |
|Cost| If we expect .9 and neuron returns .8 then cost = 0.5( .9 -.8)^2 = 0.05|
|pdW(x)-Cost|     Partial derivative  of weight with respect  to Cost  <br>
|pdW(x)-Z|        Partial derivative  of weight with respect  to Z
|pdZ-Sigmoid|     Partial derivative  of Z with respect  to Sigmoid
|pdSigmoid-Cost|  Partial derivative  of Sigmoid  respect  to Cost
|Error |          Useful variable  pdZ-Sigmoid *  pdSigmoid-Cost

We need to find a small change in each weight that will improve the cost
We change them in proportion to pdW(x)-Cost
There are some funky maths to prove this but for the outputlayer
 pdW(x)-Cost = input(x) *(sigMoid - T) * sigMoid * (1 - sigMoid)
       
 ### some funky maths
       
Using chain rule pdW(x)-Cost =  
 pdW(x)-Sigmoid * pSigmoid-Cost  = 
 pdW(x)-Z  *  pdZ-Sigmoid * pdSigmoid-Cost
 
 As in a small change in weight produces a small change in Z which produces a small change in Sigmoid which reduces the cost 
 The maths below prove that 
 
 Small Change in W(x)  dw(x) = Input(x)
 Small change in Z dZ =   sigMoid * (1 - sigMoid)
 Small change in Sigmoid improves cost by t -Sigmoid
  
 
 Mad but true.   As Cost is computed from Sigmoid which in turn is computed from Z which in turn is computed using W(x)

 pdW(x)-Z is just   input(x) as we can treat the other coeffs in Z as constants ( we are only making a small change in W(x) and therefore they have a derivative of 0
 pdZ-Sigmoid is more fun  Sigmoid = 1/(1 + e<sup>-Z</sup>)
 Reciprocal Rule  derivative of 1/f =   -df/(f<sup>2</sup>)    
 f =  1 + e<sup>-Z</sup>
  
 df  = -e<sup>-Z</sup>    
 f^2 = (1 + e<sup>-Z</sup>) * (1 + e<sup>-Z</sup>)  
 
 so -df/(f^2) =
   e^-z/[(1 + e^-Z)^2]
   
 For fun and to get what we are looking for we add and subtract 1 above the line
 
 (1 + e<sup>-Z</sup> -1)/[(1 + e<sup>-Z</sup>)<sup>2</sup>]
 
 =
  (1 + e<sup>-Z</sup>)/[(1 + e<sup>-Z</sup>)<sup>2</sup>]  - 1/[(1 + e<sup>-Z</sup>)<sup>2</sup>]
  
  we can divide the first part above and below by (1 + e<sup>-Z</sup>) giving us
  1/(1 + e<sup>-Z</sup>) - 1/[(1 + e<sup>-Z</sup>)<sup>2</sup>]
  
  which is equal to
  [1/(1 + e<sup>-Z</sup>)] ( 1 - 1/(1 + e<sup>-Z</sup>)]
  
  Of course 1/(1 + e^-Z) is Sigmoid 
  Therefore the derivative of Sigmoid is 
  Sigmoid*(1 - Sigmoid)
  
  This is useful because we can easily calculate the derivative of Sigmoid
  
  
   <math xmlns="http://www.w3.org/1998/Math/MathML">
  <mfrac>
    <mn>1</mn>
    <mi>1 + e<sup>-Z</sup></mi>
  </mfrac>
</math>
     
  1/(1 + e<sup>-Z</sup>)  -  e<sup>-Z</sup>/((1 + e<sup>-Z</sup>) * (1 + e<sup>-Z</sup>))
    
    
<math xmlns="http://www.w3.org/1998/Math/MathML">
 
  <mi>C</mi>
  <mrow class="MJX-TeXAtom-ORD">
    <mo>/</mo>
  </mrow>
 
  <msub>
    <mi>w</mi>
    <mi>k</mi>
  </msub>
</math>
