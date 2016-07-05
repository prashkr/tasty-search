var app = angular.module('app', []);

app.controller('controller', function($scope, $http) {
    $scope.reviews = [];

    $("#searchbox").bind("keypress", {}, keypressInBox);
    $("#searchbox").focus();

    function keypressInBox(e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code == 13) { //Enter keycode
            e.preventDefault();
            $(".submit-button").click();
        }
    }

    $scope.inputValue = "";
    $scope.fetchReviews = function() {
        //call api
        $http.post('/tastysearch/api/v1/search-index', $scope.inputValue)
            .success(function (data, status, headers, config) {
                $scope.reviews = data;
            })
            .error(function (data, status, header, config) {
                //todo
            });
    };
});