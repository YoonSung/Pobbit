var canvasSize = 0;
var sideWidth = 0;

// 숫자들 정리를 하고 싶다...
var hexRadius = 0;
var hexYPoint = 0;
var hexYPointHalf = 0;
var hexXPoint = 0;

let pointArray = new Array();

// 그래프 영역 총괄
// todo: const와 let을 써야 한다는 정보 es5 이후
let mainPIXIApp;

const baseColor = '333333';
const leftColor = 'bda1f4';
const rightColor = 'fbd9a6';

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

  // todo: insert selection logic
  drawSample();

  makeCongressManInfo();
  makeCongressManInfo('이언주', 'right');
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

  hexRadius = canvasSize/2.5;
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

  //graph.drawPolygon([
  //      0, -hexYPoint * factor1,  //12
  //      hexXPoint * factor2, -hexYPointHalf * factor2,  //2
  //      hexXPoint * factor3, hexYPointHalf * factor3, //4
  //      0, hexYPoint * factor4, //6
  //      -hexXPoint * factor5, hexYPointHalf * factor5,  //8
  //      -hexXPoint * factor6, -hexYPointHalf * factor6, //10
  //]);

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

// 컨테이너에 담아서 처리는 하는 것이 좋을 것 같음
function makeClassifyNamePlate()
{
  // container를 만들어

  // 6개의 포인트를 배치하고

  ////////////////////////////////////////////////
  // sample code
  //var attendant = new PIXI.Text('출석률', { font: '16px Snippet', fill: 'white', align: 'center' });
  // 중점을 기준으로 움직이게 만듦
  //attendant.anchor.set(0.5, 0.5);
  //attendant.x = 0;
  //attendant.y = 0;
  //mainPIXIApp.stage.addChild(attendant);
}


var photoMap = new Map();
function makeCongressManInfo(name="김무성", side="left")
{

  //console.log(photoMap.get(name));
  $("#"+side+"photo").css('background','#'+baseColor);
  $("#"+side+"photo").css('height',sideWidth * 1.35);
  $("#"+side+"photo").css('text-align','center');
  $("#"+side+"photo").css('padding-top',(sideWidth * 1.35 - sideWidth * 1.25)/2);

  $("#"+side+"photo").html('<img src="' + photoMap.get(name) + '" width="' + sideWidth * 0.9 + '" height="' + sideWidth * 1.25 +'" align="middle" />');

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
var fullCharged = [100,100,100,100,100,100];
var someSenator = [80, 40, 30, 10, 90, 10];

function drawSample()
{
  drawStat(false, leftColor, someSenator[0]/fullCharged[0], 0.8, 0.7, 0.6, 0.9, 0.7);
  drawStat(false, rightColor, 0.7, 0.8, 0.9, 0.7, 0.3, 0.6);
}

photoMap.set("김무성", "http://cdn.mirror.wiki/http://info.nec.go.kr/photo_20160413/Sd2600/Gsg2604/Sgg2260401/Hb100120095/gicho/100120095.JPG")
photoMap.set("이언주", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-NCMDZZx_l7vSfoYz9EE32-9XTCTyQ4MjctZhiegaFFBj1WeU")
///////////////////////////////////////////////////
