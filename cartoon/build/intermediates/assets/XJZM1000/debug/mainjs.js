        function testjs(){
            alert('initJS');
        }

        var imgsNum;
        function initNum() {
            alert('init');
            if(null == document.getElementsByTagName("img")){
                setTimeout("alert('1 seconds!')",1000);
            }else{
                imgsNum = document.getElementsByTagName("img").length;
                var imgs = document.getElementsByTagName("img")
                for (var i = 0 ; i < imgs.length ; i++){
                    imgs[i].onload =count();
                }
            }
        }

        function count() {
            imgsNum--;
            if (0 == imgsNum){
                alert('loaded');
            }
        }
