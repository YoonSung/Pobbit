var canvasSize = 0;
var sideWidth = 0;

// 숫자들 정리를 하고 싶다...
var hexRadius = 0;
var hexYPoint = 0;
var hexYPointHalf = 0;
var hexXPoint = 0;

var pointArray = new Array();

// 그래프 영역 총괄
// todo: const와 let을 써야 한다는 정보 es5 이후
var mainPIXIApp;


///////////////////////////////////////////////////
// global static const data
// Do Not Modify/Delete This!
var tableRowName = ["출석률", "법안 발의 횟수", "준법 정신(전과 현황)", "공개 재산","후원금" ,"경력"];
var baseColor = '333333';
var leftColor = 'bda1f4';
var rightColor = 'fbd9a6';
///////////////////////////////////////////////////


window.addEventListener("load", loadServerData, false);
window.addEventListener("resize", refresh);

function loadServerData()
{
  initServerDatas();
  start();
}

function start()
{
  initData();

  // draw PIXI
  mainPIXIApp = new PIXI.Application(canvasSize, canvasSize, {backgroundColor : baseColor},{antialias:true});

  // base hexagon drawing
  $("#hexagon").append(mainPIXIApp.view);
  drawStat(true);

  // todo : 삭제 해야 합니다!
  // 샘플 그래프 그리는 함수
  drawSample();

  //$('.list-group').css('max-height', $(window).height());
  //console.log($('.list-group'));
  $('.list-group').each(function(index, value) {
    //console.log(index, value);
    value.style.maxHeight = $(window).height() * 0.5;
  });

  // side menu drawing
  $.each(congressName, function(index, value) {
    $('#leftList .list-group').append('<a href="#" class="list-group-item">'+ value +'</a>');
    $('#rightList .list-group').append('<a href="#" class="list-group-item">'+ value +'</a>');
  });

  // todo : 국회의원 사진 넣는 함수인데, 여기가 아니라 리스트 선택 함수로 옮겨져야 합니다.
  makeCongressManInfo();
  makeCongressManInfo('이언주', 'right');

  // todo : 테이블 구성 함수인데, 여기가 아니라 리스트 선택 함수로 옮겨져야 합니다.
  AddTableRow();
}

function refresh()
{
  $("#hexagon").empty();
  start();
}

// 그리기용 local data만 initialize 한다
function initData()
{
  canvasSize = document.getElementById('hexagon').clientWidth;

  sideWidth = document.getElementsByClassName('congressman')[0].clientWidth;
//  console.log(sideWidth);

  hexRadius = canvasSize/4;
  hexYPoint = hexRadius;
  hexYPointHalf = hexYPoint / 2;
  hexXPoint = hexYPoint / 2 * Math.sqrt(3);
}

// 매번 로드 하지 말 것
function initServerDatas()
{
  // todo: 국회의원 전체 리스트 가져오기

  // 여기서 할 게 아닌듯
  // todo: 국회의원 photomap 제작하기
  // 이건 매번 할게 아니라 한 번 조회할 때 마다 캐싱하는게 좋을듯

  // todo: 국회의원 stat 정보 저장하기(방식은 생각 좀)
  // 이 또한 캐싱 전략으로 가는게 좋겠다.
}

function drawStat(isBase = false, lineColor = 'ababab',
                   factor1 = 1,
                   factor2 = 1,
                   factor3 = 1,
                   factor4 = 1,
                   factor5 = 1,
                   factor6 = 1)
{
  let graph = new PIXI.Graphics();
  graph.interactive = false;
  graph.buttonMode = false;

  lineColor = '0x'+lineColor;
  graph.lineStyle(2, lineColor, 1);

  // 아 너무 구리지만 일단...
  // 사실 base 만들 때만 쓰긴 하는데...
  pointArray[0] = {x:0, y:-hexYPoint * factor1};
  pointArray[1] = {x:hexXPoint * factor2, y:-hexYPointHalf * factor2};
  pointArray[2] = {x:hexXPoint * factor3, y:hexYPointHalf * factor3};
  pointArray[3] = {x:0, y:hexYPoint * factor4};
  pointArray[4] = {x:-hexXPoint * factor5, y:hexYPointHalf * factor5};
  pointArray[5] = {x:-hexXPoint * factor6, y:-hexYPointHalf * factor6};

  graph.drawPolygon([
    pointArray[0].x, pointArray[0].y,
    pointArray[1].x, pointArray[1].y,
    pointArray[2].x, pointArray[2].y,
    pointArray[3].x, pointArray[3].y,
    pointArray[4].x, pointArray[4].y,
    pointArray[5].x, pointArray[5].y
  ]);

  // make base layer
  if (isBase)
  {
    graph.lineStyle(1, lineColor, 0.2).moveTo(0, hexYPoint).lineTo(0, -hexYPoint);
    graph.lineStyle(1, lineColor, 0.2).moveTo(hexXPoint, hexYPointHalf).lineTo(-hexXPoint, -hexYPointHalf);
    graph.lineStyle(1, lineColor, 0.2).moveTo(hexXPoint, -hexYPointHalf).lineTo(-hexXPoint, hexYPointHalf);

    // 6방향 이름
    makeClassifyNamePlate();
  }

  graph.endFill();

  // move graph to center
  graph.x = canvasSize /2;
  graph.y = canvasSize /2;

  mainPIXIApp.stage.addChild(graph);
}

// 한글로 나오는 항목 이름을 PIXI에 출력하는 함수
// 한글 엣지부분이 흐릿한 이유는 아직 잘 모르겠음
function makeClassifyNamePlate()
{
  // container를 만들어, 중점 중앙으로 이동하고 main app에 붙임
  var letterContainer = new PIXI.Container();
  letterContainer.x = canvasSize/2;
  letterContainer.y = canvasSize/2;
  mainPIXIApp.stage.addChild(letterContainer);

  // 6개의 포인트를 배치
  var addPointRatio = 0;
  for (var i in pointArray)
  {
    var addPointRatio = 0;
    var textObject = new PIXI.Text('', { font: '16px', fill: 'white', align: 'center' });

    switch (i) {
      case '0':
        textObject.setText('출석');
        addPointRatio = 1.2;
        break;
      case '1':
        textObject.setText('경력');
        addPointRatio = 1.3;
        break;
      case '2':
        textObject.setText('법안발의');
        addPointRatio = 1.3;
        break;
      case '3':
        textObject.setText('준법정신');
        addPointRatio = 1.2;
        break;
      case '4':
        textObject.setText('공개재산');
        addPointRatio = 1.3;
        break;
      case '5':
        textObject.setText('후원금');
        addPointRatio = 1.3;
        break;
      default:
        break;
    }

    textObject.anchor.set(0.5, 0.5);
    textObject.x = pointArray[i].x * addPointRatio;
    textObject.y = pointArray[i].y * addPointRatio;
    letterContainer.addChild(textObject);
  }
}


function AddTableRow() {
  // for (var i in tableRowName)
  // {
  //   var row = $("<tr><td>"+tableRowName[i]+"</td><td>"+1+"</td><td>"+2+"</td></tr>");
  //   $('.table tbody').append(row);
  // }

  // 그래프에서는 비율을 넣어줘야 하는데, 여기에서는 original 값을 넣어줘야 함
  // 그래프 계산식 = original 값 / 각 항목 맥스
  // Table 계산식 = original 값
  // todo : data structure 만들어 지면 읽어와서 넣을 것
  $.each(tableRowName, function(index, value) {
    var row = $("<tr><td>"+ value +"</td><td>"+ 1 +"</td><td>"+ 2 +"</td></tr>");
    $('.table tbody').append(row);
  });
}

// click test function
// todo : 정상적으로 만들어야지...
$( document ).ready(function() {
  $('#leftList').on('click', function() {
    console.log("1");
  })
});


// 사진과 양쪽 base color를 넣는 함수입니다만
// todo : color를 고정하기로 했기 때문에 color는 css에 박고 삭제할 필요가 있습니다.
var photoMap = new Map();
function makeCongressManInfo(name="김무성", side="left")
{
  //console.log(photoMap.get(name));
  $("#"+side+"photo").css('background','#'+baseColor);
  //$("#"+side+"photo").css('height',sideWidth * 1.35);
  //$("#"+side+"photo").css('text-align','center');
  //$("#"+side+"photo").css('padding-top',(sideWidth * 1.35 - sideWidth * 1.25)/2);

  $("#"+side+"photo").html('<img src="' + photoMap.get(name) + '" width="' + sideWidth + '" height="' + sideWidth * 1.1 +'" align="middle" />');

  // left right만 구분지어서 하기로 했음
  if (side == "left") {
    $("#"+side+"color").css('background','#'+leftColor);
  }
  else {
    $("#"+side+"color").css('background','#'+rightColor);
  }

  //$("#"+side+"color").css('max-height', 15);
  $("#"+side+"color").css('height', 15);
}



///////////////////////////////////////////////////
// sample datas
// todo : delete and make real funtion
var fullCharged = [100,100,100,100,100,100];
var someSenator = [80, 40, 30, 10, 90, 10];

function drawSample()
{
  drawStat(false, leftColor, 
    someSenator[0]/fullCharged[0], 
    someSenator[1]/fullCharged[1], 
    someSenator[2]/fullCharged[2], 
    someSenator[3]/fullCharged[3], 
    someSenator[4]/fullCharged[4], 
    someSenator[5]/fullCharged[5]
  );
  drawStat(false, rightColor, 0.7, 0.8, 0.9, 0.7, 0.3, 0.6);
}

var congressName = ['김무성', '이언주', '강병원', '강창일', '강훈식', '고용진', '권미혁', '권칠승', '금태섭', '기동민', '김경수', '김경협', '김두관', '김민기', '김병관'];

photoMap.set("김무성", "http://cdn.mirror.wiki/http://info.nec.go.kr/photo_20160413/Sd2600/Gsg2604/Sgg2260401/Hb100120095/gicho/100120095.JPG")
photoMap.set("이언주", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-NCMDZZx_l7vSfoYz9EE32-9XTCTyQ4MjctZhiegaFFBj1WeU")
///////////////////////////////////////////////////
