function [J grad] = nnCostFunction(nn_params, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, ...
                                   X, y, lambda)
%NNCOSTFUNCTION Implements the neural network cost function for a two layer
%neural network which performs classification
%   [J grad] = NNCOSTFUNCTON(nn_params, hidden_layer_size, num_labels, ...
%   X, y, lambda) computes the cost and gradient of the neural network. The
%   parameters for the neural network are "unrolled" into the vector
%   nn_params and need to be converted back into the weight matrices. 
% 
%   The returned parameter grad should be a "unrolled" vector of the
%   partial derivatives of the neural network.
%

% Reshape nn_params back into the parameters Theta1 and Theta2, the weight matrices
% for our 2 layer neural network
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

% Setup some useful variables
m = size(X, 1);
         
% You need to return the following variables correctly 
J = 0;
Theta1_grad = zeros(size(Theta1));
Theta2_grad = zeros(size(Theta2));

% ====================== YOUR CODE HERE ======================
% Instructions: You should complete the code by working through the
%               following parts.
%
% Part 1: Feedforward the neural network and return the cost in the
%         variable J. After implementing Part 1, you can verify that your
%         cost function computation is correct by verifying the cost
%         computed in ex4.m
%
% Part 2: Implement the backpropagation algorithm to compute the gradients
%         Theta1_grad and Theta2_grad. You should return the partial derivatives of
%         the cost function with respect to Theta1 and Theta2 in Theta1_grad and
%         Theta2_grad, respectively. After implementing Part 2, you can check
%         that your implementation is correct by running checkNNGradients
%
%         Note: The vector y passed into the function is a vector of labels
%               containing values from 1..K. You need to map this vector into a 
%               binary vector of 1's and 0's to be used with the neural network
%               cost function.
%
%         Hint: We recommend implementing backpropagation using a for-loop
%               over the training examples if you are implementing it for the 
%               first time.
%
% Part 3: Implement regularization with the cost function and gradients.
%
%         Hint: You can implement this around the code for
%               backpropagation. That is, you can compute the gradients for
%               the regularization separately and then add them to Theta1_grad
%               and Theta2_grad from Part 2.
%

%%%%%%%%%%%%%%%% PART 1 %%%%%%%%%%%%%%%%%%

% rename num_labels to K
K = num_labels;

% add column of ones to X (input layer) (5000x401 matrix)
X = [ones(size(X,1),1) X];

% compute hidden layer from input layer a1=X
z2 = X*Theta1';   %' Theta1 is 401x25
a2 = sigmoid(z2); % a2 is 5000x25

% add column of ones to hidden layer
a2 = [ones(size(a2,1),1) a2]; % now 5000x26

% compute output layer h=a3 from hidden layer
z3 = a2*Theta2';  %' Theta2 is 10x26
h = sigmoid(z3);  % h is 5000x10

% make new y array ---> 5000x1 to 5000x10
newy = zeros(m,K);
for i = 1:m
	newy(i,:) = y(i) == (1:K);
end

% loop over examples and compute cost
for e = 1:m
    J += (-1/m)*( newy(e,:)*log( h(e,:)' ) +  (1-newy(e,:))*log( 1- h(e,:)' ) );
end

% modify thetas for regularization
Theta1_reg = Theta1;
Theta1_reg(:,1) = zeros(size(Theta1(:,1)));
Theta2_reg = Theta2;
Theta2_reg(:,1) = zeros(size(Theta2(:,1)));

% regularize J
J_reg = lambda/(2.0*m) * ( sum(sum(Theta1_reg.^2)) + sum(sum(Theta2_reg.^2)) );
J += J_reg;

%%%%%%%%%%%%%%%% PART 2 %%%%%%%%%%%%%%%%%%

%Delta1 = zeros(size(Theta1));
%Delta2 = zeros(size(Theta2));

% loop over all training examples
for t = 1:m

    % step 1
    a1 = X(t,:);           %  1x401 input layer
    z2 = a1*Theta1';       %' 1x25
	a2 = [1 sigmoid(z2)];  %  1x26 hidden layer
	z3 = a2*Theta2';       %' 1x10
	a3 = sigmoid(z3);      %  1x10 output layer
	
	% step 2
	d3 = a3 - newy(t,:);   %  1x10 delta3
	
	% step 3
	d2 = (d3*Theta2)(2:end) .* sigmoidGradient(z2);   %  1x25
	
	% step 4
%	Delta1 += d2'*a1;      %'  25x401
%	Delta2 += d3'*a2;      %'  10x26
	
	% step 5
%	Theta1_grad = Delta1 / m;
%	Theta2_grad = Delta2 / m;
	
	% combine step 4 and 5 into one step
	Theta1_grad += d2'*a1 / m;   %'  25x401
	Theta2_grad += d3'*a2 / m;   %'  10x26
	
end

%%%%%%%%%%%%%%%% PART 3 %%%%%%%%%%%%%%%%%%

% regularize gradients
Theta1_grad += (lambda/m) * Theta1_reg;   %  25x401
Theta2_grad += (lambda/m) * Theta2_reg;   %  10x26


% -------------------------------------------------------------

% =========================================================================

% Unroll gradients
grad = [Theta1_grad(:) ; Theta2_grad(:)];


end
