function centroids = computeCentroids(X, idx, K)
%COMPUTECENTROIDS returs the new centroids by computing the means of the 
%data points assigned to each centroid.
%   centroids = COMPUTECENTROIDS(X, idx, K) returns the new centroids by 
%   computing the means of the data points assigned to each centroid. It is
%   given a dataset X where each row is a single data point, a vector
%   idx of centroid assignments (i.e. each entry in range [1..K]) for each
%   example, and K, the number of centroids. You should return a matrix
%   centroids, where each row of centroids is the mean of the data points
%   assigned to it.
%

% Useful variables
[m n] = size(X);

% You need to return the following variables correctly.
centroids = zeros(K, n);

% ====================== YOUR CODE HERE ======================
% Instructions: Go over every centroid and compute mean of all points that
%               belong to it. Concretely, the row vector centroids(i, :)
%               should contain the mean of the data points assigned to
%               centroid i.
%
% Note: You can use a for-loop over the centroids to compute this.
%

% loop over centroids for updating
for i = 1:K,

    % initialize variables for each centroid
    csum = zeros(1,n);
    count = 0;

    % loop over examples to find those assigned to current centroid
    for j = 1:m,

        % if current examples centroid index is for this centroid
        if idx(j) == i,
    		csum += X(j,:);
    		count += 1;
		end;
	end;

	% normalize by number of examples found for current centroid
	csum = csum / count;
	
	% update centroid position
	centroids(i,:) = csum;

end;

% =============================================================


end

