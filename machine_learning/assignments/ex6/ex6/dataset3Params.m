function [C, sigma] = dataset3Params(X, y, Xval, yval)
%EX6PARAMS returns your choice of C and sigma for Part 3 of the exercise
%where you select the optimal (C, sigma) learning parameters to use for SVM
%with RBF kernel
%   [C, sigma] = EX6PARAMS(X, y, Xval, yval) returns your choice of C and 
%   sigma. You should complete this function to return the optimal C and 
%   sigma based on a cross-validation set.
%

% You need to return the following variables correctly.
C = 1;
sigma = 0.3;

% ====================== YOUR CODE HERE ======================
% Instructions: Fill in this function to return the optimal C and sigma
%               learning parameters found using the cross validation set.
%               You can use svmPredict to predict the labels on the cross
%               validation set. For example, 
%                   predictions = svmPredict(model, Xval);
%               will return the predictions on the cross validation set.
%
%  Note: You can compute the prediction error using 
%        mean(double(predictions ~= yval))
%

% range of C & sigma values to try
vals = [0.01, 0.03, 0.1, 0.3, 1, 3, 10, 30];

% set r0 as larger number initially
r0 = 1000000000;

% loop over vals for C
for i=1:length(vals),

    % set C to current val
	C = vals(i);

    % loop over vals for sigma
    for j=1:length(vals),

        % set sigma to square root of current val
		sigma = vals(j)^0.5;

        % train to get model for predictions
        model = svmTrain(X, y, C, @(x1, x2) gaussianKernel(x1, x2, sigma));
        predictions = svmPredict(model, Xval);
        r = mean(double(predictions ~= yval));

        % track for lowest mean ---> optimized C & sigma
        if r < r0,
			r0 = r;
		    C0 = C;
			sigma0 = sigma;
		end;
        
	end;
end;

% set C & sigma to optimized values
C = C0;
sigma = sigma0;

% =========================================================================

end
