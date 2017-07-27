var canvasSize = 0;
var sideWidth = 0;

var hexRadius = 0;
var hexYPoint = 0;
var hexYPointHalf = 0;
var hexXPoint = 0;
var app;

var graphColor = '333333';
var sideBackColor = '333333';
var koreaParty = 0;
var liberalParty = 0;
var uprightParty = 0;
var nationParty = '048300';

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
  app = new PIXI.Application(canvasSize, canvasSize, {backgroundColor : graphColor},{antialias:true});

  // base hexagon drawing
  $("#hexagon").append(app.view);
  drawStat(true);

  // todo: insert selection logic
  drawSample();
  makeCongressManInfo();
  makeCongressManInfo('이언주', 'right', nationParty);
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

function drawStat(isBase = false, lineColor = 0xababab,
                   factor1 = 1,
                   factor2 = 1,
                   factor3 = 1,
                   factor4 = 1,
                   factor5 = 1,
                   factor6 = 1)
{
  var graph = new PIXI.Graphics();
  graph.interactive = false;
  graph.buttonMode = false;

  graph.lineStyle(2, lineColor, 1);
  graph.drawPolygon([
        0, hexYPoint * factor4,
        hexXPoint * factor3, hexYPointHalf * factor3,
        hexXPoint * factor2, -hexYPointHalf * factor2,
        0, -hexYPoint * factor1,
        -hexXPoint * factor6, -hexYPointHalf * factor6,
        -hexXPoint * factor5, hexYPointHalf * factor5,
      ]);

  if (isBase)
  {
    graph.lineStyle(1, lineColor, 0.2).moveTo(0, hexYPoint).lineTo(0, -hexYPoint);
    graph.lineStyle(1, lineColor, 0.2).moveTo(hexXPoint, hexYPointHalf).lineTo(-hexXPoint, -hexYPointHalf);
    graph.lineStyle(1, lineColor, 0.2).moveTo(hexXPoint, -hexYPointHalf).lineTo(-hexXPoint, hexYPointHalf);
  }

  graph.endFill();

  graph.x = canvasSize /2;
  graph.y = canvasSize /2;

  app.stage.addChild(graph);
}

var photoMap = new Map();
function makeCongressManInfo(name="김무성", side="left", inColor='048300')
{

  //console.log(photoMap.get(name));
  $("#"+side+"photo").css('background','#'+sideBackColor);
  $("#"+side+"photo").css('height',sideWidth * 1.35);
  $("#"+side+"photo").css('text-align','center');
  $("#"+side+"photo").css('padding-top',(sideWidth * 1.35 - sideWidth * 1.25)/2);

  $("#"+side+"photo").html('<img src="' + photoMap.get(name) + '" width="' + sideWidth * 0.9 + '" height="' + sideWidth * 1.25 +'" align="middle" />');

  // 당 색상을 넣어준다
  $("#"+side+"color").css('background','#'+inColor);
  //$("#"+side+"color").css('max-height', 15);
  $("#"+side+"color").css('height', 15);

}




///////////////////////////////////////////////////
// sample datas
var fullCharged = [100,100,100,100,100,100];
var someSenator = [80, 40, 30, 10, 90, 10];

function drawSample()
{
  drawStat(false, 0xf7c785, someSenator[0]/fullCharged[0], 0.8, 0.7, 0.6, 0.9, 0.7);
  drawStat(false, 0x32cc5b, 0.7, 0.8, 0.9, 0.7, 0.3, 0.6);
}

photoMap.set("김무성", "http://cdn.mirror.wiki/http://info.nec.go.kr/photo_20160413/Sd2600/Gsg2604/Sgg2260401/Hb100120095/gicho/100120095.JPG")
photoMap.set("이언주", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-NCMDZZx_l7vSfoYz9EE32-9XTCTyQ4MjctZhiegaFFBj1WeU")
///////////////////////////////////////////////////
