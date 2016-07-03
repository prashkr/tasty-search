var app = angular.module('app', []);

app.controller('controller', function($scope, $http) {
    $scope.reviews = [];

    $("#searchbox").bind("keypress", {}, keypressInBox);

    function keypressInBox(e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if (code == 13) { //Enter keycode
            e.preventDefault();
            $(".submit-button").click();
        }
    }

    $scope.inputValue = "";
    $scope.fetchReviews = function() {
        var tokens = $scope.inputValue.split(" ");

        //call api
        $http.post('/application/api/v1/search', tokens)
            .success(function (data, status, headers, config) {
                $scope.reviews = data;
            })
            .error(function (data, status, header, config) {
                //todo
            });
    };
});