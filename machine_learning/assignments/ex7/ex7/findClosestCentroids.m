function idx = findClosestCentroids(X, centroids)
%FINDCLOSESTCENTROIDS computes the centroid memberships for every example
%   idx = FINDCLOSESTCENTROIDS (X, centroids) returns the closest centroids
%   in idx for a dataset X where each row is a single example. idx = m x 1 
%   vector of centroid assignments (i.e. each entry in range [1..K])
%

% Set K
K = size(centroids, 1);

% You need to return the following variables correctly.
idx = zeros(size(X,1), 1);

% ====================== YOUR CODE HERE ======================
% Instructions: Go over every example, find its closest centroid, and store
%               the index inside idx at the appropriate location.
%               Concretely, idx(i) should contain the index of the centroid
%               closest to example i. Hence, it should be a value in the 
%               range 1..K
%
% Note: You can use a for-loop over the examples to compute this.
%

m = length(X)

% loop over examples in X
for i = 1:m,

    % initialize dmin and closest
	dmin = sum( (X(i,:) - centroids(1,:)).^2 );
    closest = 1;

    % loop over rest of centroids
    for j = 2:K,

        % compute squared distance to centroid
		d = sum( (X(i,:) - centroids(j,:)).^2 );

        % if current centroid is closer, update
        if d < dmin,
			dmin = d;
    		closest = j;
		end;
	end;
	
	% set the index to the closest found centroid
	idx(i) = closest;

end;

% =============================================================

end

