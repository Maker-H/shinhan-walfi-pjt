import { useRef, useState, useEffect, useMemo } from "react";
import { StyleSheet, Text, View, Image } from "react-native";
import { useDispatch, useSelector } from "react-redux";

import { RFPercentage } from "react-native-responsive-fontsize";
import * as Animatable from "react-native-animatable";
import { AntDesign } from "@expo/vector-icons";

import ExpBar from "./exp/ExpBar";
import { images } from "../../../common/imgDict.js";
import { SCREEN_WIDTH, SCREEN_HEIGHT } from "../homecomponents/ScreenSize.js";

// 추후에 여기 컴포넌트로 분리, style 파일 props로 전달 필요!

function ShwoExchange({
  통화코드: ntnCode,
  매매기준환율: exchangeRate,
  전일대비: compareDt,
}) {
  const sholdShowCreatedown = compareDt < 0;
  const trim_compareDt = sholdShowCreatedown ? compareDt * -1 : compareDt;
  // const num_exchangeRate = exchangeRate.toLocaleString('es-US')
  return (
    <View
      style={{
        flexDirection: "row",
        width: "100%",
        justifyContent: "space-between",
        paddingHorizontal: "10%",
        alignItems: "center",
      }}
    >
      <View>
        <Text style={{ color: "white", fontWeight: "bold" }}>
          {ntnCode + " "}
        </Text>
      </View>
      <Text style={{ color: "white", fontWeight: "bold" }}>
        {exchangeRate + " "}
      </Text>
      <View style={{ flexDirection: "row", alignItems: "center" }}>
        {sholdShowCreatedown ? (
          <AntDesign name="caretdown" size={RFPercentage(1)} color="#293694" />
        ) : (
          <AntDesign name="caretup" size={RFPercentage(1)} color="red" />
        )}
        <Text
          style={{
            color: sholdShowCreatedown ? "#293694" : "red",
            fontWeight: "bold",
          }}
        >{`(${trim_compareDt})`}</Text>
      </View>
    </View>
  );
}

const TextTransition = (props) => {
  // 나중에 이 부분 최적화 잡기.
  const exchanges = Object.values(
    useSelector((state) => state.wallet.exchangeRates)
  );
  const [currentId, setCurrentId] = useState(0);

  useEffect(() => {
    const timeout = setInterval(() => {
      setCurrentId((prev) => (prev + 1) % 5);
    }, 1000); // 3초마다 텍스트 변경
    return () => clearTimeout(timeout);
  }, [currentId]);

  return (
    <View style={styles.rightBottomBox}>
      {exchanges && <ShwoExchange {...exchanges[currentId]} />}
    </View>
  );
};

function GameHeader(props) {
  //   console.log("header");
  const {userGameInfo:gameUser, mainCharacter} = useSelector((state) => state.home);
  return (
    <>
      <View style={styles.header}>
        <View style={{ flexDirection: "column", flex: 1 }}>
          <View style={styles.headerItem}>
            {/* <View style={{ flex: 1, alignItems: 'center' }}>
                <Image source={images.defaultCharacter.TIGER.MINT} style={styles.profile}></Image>
            </View> */}
            <Text style={styles.name}>{gameUser&&gameUser.username}</Text>
          </View>
          <View
            style={{
              flex: 1,
              justifyContent: "center",
              alignItems: "center",
            }}
          >
          <ExpBar ExpStyle={ExpStyle}exp={mainCharacter.exp} level={mainCharacter.level}/>
            
          </View>
        </View>
        <View style={{ flexDirection: "column", flex: 1 }}>
          <View style={styles.headerRight}>
            <View style={styles.rightTopBox}>
              <Image
                source={images.gameIcon.coin}
                style={{ resizeMode: "contain", height: "85%", width: "20%" }}
              />
              <Text
                style={{
                  fontSize: 18,
                  fontWeight: "bold",
                  color: "white",
                  height: "85%",
                  marginEnd: "10%",
                }}
              >
                {" "}
                {gameUser.point}
              </Text>
            </View>
          </View>
          <View style={styles.headerRight}>
            <TextTransition />
          </View>
        </View>
      </View>
    </>
  );
}

export default GameHeader;


const styles = StyleSheet.create({
  header: {
    flex: 1,
    width: SCREEN_WIDTH,
    flexDirection: "row",
    backgroundColor: "rgba(41, 54, 148, 0.8)",
  },
  headerItem: {
    flex: 2,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
  },
  // profile: {
  //     width: '65%',
  //     height: '85%',
  //     backgroundColor: '#0F6828',
  //     borderRadius: 20,
  //     borderColor: '#5C4800',
  //     borderWidth: 2,
  //     resizeMode: 'contain',
  //     marginTop: '10%',
  // },
  name: {
    flex: 1.5,
    fontWeight: "bold",
    fontSize: 25,
    color: "white",
    textShadowColor: "#272B49",
    textShadowRadius: 2,
    textShadowOffset: { width: 2, height: 2 },
    elevation: 1,
    marginTop: "5%",
    marginStart: "10%",
  },
  headerRight: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
  },
  rightTopBox: {
    flex: 1,
    flexDirection: "row",
    backgroundColor: "#758DCC",
    height: "80%",
    alignItems: "center",
    justifyContent: "space-between",
    marginHorizontal: "10%",
    marginTop: "3%",
    borderRadius: 20,
    borderWidth: 3,
    borderColor: "#505A75",
  },
  rightBottomBox: {
    flex: 1,
    flexDirection: "row",
    backgroundColor: "#758DCC",
    height: "80%",
    alignItems: "center",
    justifyContent: "center",
    marginHorizontal: "10%",
    marginBottom: "3%",
    borderRadius: 20,
    borderWidth: 3,
    borderColor: "#505A75",
  },
});

const ExpStyle = StyleSheet.create({
  container:{
    width:'80%',
    height: "50%",
    flexDirection:'row',
    alignItems:'center',
    justifyContent:'space-between'
  },
  LvTxt:{
    color:'white',
    alignContent:'center',
    fontWeight:'bold',
    textShadowColor: "#272B49",
    textShadowRadius: 2,
    textShadowOffset: { width: 2, height: 2 },
  },
  barContainer: {
      width: "70%",
      height: "80%",
      backgroundColor: "#D9D9D9",
      borderRadius: 15,
  },
  exp:{
    backgroundColor:'#F6B101',
    borderRadius: 15,
    height:'100%',
  }
})
