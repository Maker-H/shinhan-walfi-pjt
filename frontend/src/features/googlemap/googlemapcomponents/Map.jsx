import { useDispatch, useSelector } from "react-redux";
import { useState, useEffect } from "react";
import images from "../../../common/imgDict";
import axios from "../../../common/http-common";
import { useLocation } from "../googlemaphooks/UseMap";
import { useNavigation } from "@react-navigation/native";
import { setEnemy, setPlayer } from "./../../../actions/animalAction";
import MapView, { PROVIDER_GOOGLE, Marker } from "react-native-maps";
import { setMaxHpBar, setBankIdx } from "../../../actions/loadingActions";
import {
  ScreenHeight,
  ScreenWidth,
} from "./../../fight/fightcomponents/ScreenSize";
import {
  StyleSheet,
  View,
  Modal,
  Text,
  TouchableOpacity,
  Image,
  Button,
} from "react-native";

const Map = () => {
  const [isModalVisible, setModalVisible] = useState(false);
  const myLocation = useLocation(null);
  const [banks, setBanks] = useState(null);
  const [selectedBankDetails, setSelectedBankDetails] = useState(null);
  const [region, setRegion] = useState(null);
  const [myAnimal, setmyAnimal] = useState(null);
  const [exchange, setexchange] = useState(null);
  const navigation = useNavigation();
  const dispatch = useDispatch();
  const userId = useSelector(state=>state.auth.userId)

  useEffect(() => {
    // 내 좌표 기준 주변 은행
    if (myLocation) {
      const newRegion = {
        latitude: myLocation[0],
        longitude: myLocation[1],
        latitudeDelta: 0.01,
        longitudeDelta: 0.01,
      };
      setRegion(newRegion); // 이거 해야 내위치 지도상에 보임
      const fetchBanks = async () => {
        try {
          const response = await axios.post(`/branch`, {
            latitude: myLocation[0],
            longitude: myLocation[1],
          });
          const data = response.data;
          setBanks(data.data);
        } catch (error) {
          console.error(
            "서버에서 은행 정보를 가져오는 중 오류가 발생했습니다.",
            error
          );
        }
      };
      fetchBanks();
    }
  }, [myLocation]);

  const handleMarkerPress = (bank) => {
    fetchDataOnMarkerPress(bank);
  };

  const fetchDataOnMarkerPress = async (bank) => {
    try {
      // 지도상에 표시된 은행중 하나 누름
      await fetchBankDetail(bank.지점번호); // 해당 은행의 상세정보를 서버에서 받아옴
      await playerAnimal(); // 내 메인 동물 정보 받아옴
      await todayExchange(); // 환율정보 받아옴
      setModalVisible(true); // 지점장과 은행정보 모달
    } catch (error) {
      console.error("데이터를 가져오는 중 오류가 발생했습니다.", error);
    }
  };

  const fetchBankDetail = async (idx) => {
    // 은행 상세정보
    try {
      const response = await axios.get(`/branch?idx=${idx}`);
      const data = response.data;
      
      dispatch(setBankIdx(idx));
      setSelectedBankDetails(data.data);
    } catch (error) {
      console.error(
        "서버에서 은행 상세 정보를 가져오는 중 오류가 발생했습니다.",
        error
      );
    }
  };

  const playerAnimal = async () => {
    // 내 메인 동물 호출
    try {
      const response = await axios.post(`/character/getmain`, {
        userId: userId,
      });
      const data = response.data;
      setmyAnimal(data.data.characterDto);
    } catch (error) {
      console.error("메인 동물 호출중 오류가 발생했습니다.", error);
    }
  };

  const todayExchange = async () => {
    // 오늘 환율 호출
    try {
      const response = await axios.get(`/exchange/info`);
      const data = response.data;
      setexchange(data.data.exchangeDtoList);
    } catch (error) {
      console.error("오늘의 환율 호출중 오류가 발생했습니다.", error);
    }
  };

  const ANIMAL_TO_CURRENCY = {
    TIGER: "KRW",
    EAGLE: "USD",
    SHIBA: "JPY",
    LION: "EUR",
    QUOKKA: "AUD",
    PANDA: "CNY",
    MOLLY: "KRW"
  };

  const setExchangeForAnimal = (animalType) => {
    const currencyCode = ANIMAL_TO_CURRENCY[animalType];
    const exchangeData = exchange.find(
      (item) => item.통화코드 === currencyCode
    );
    if (exchangeData) {
      return exchangeData.전일대비;
    }
    return 1;
  };

  const closeModal = () => {
    // 모달창 닫음
    setModalVisible(false);
  };

  const handleGoToBattle = () => {
    // 모달창에서 배틀로 이동시 동물 정보 리덕스에 저장 (manager...)
    if (selectedBankDetails != null && myAnimal != null) {
      const playerExchange = setExchangeForAnimal(myAnimal.characterType);
      const enemyExchange = setExchangeForAnimal(
        selectedBankDetails.managerAnimalType
      );

      const playerStat = {
        animal: myAnimal?.characterType,
        Level: myAnimal?.level,
        exp: myAnimal.exp,
        Hp: myAnimal?.hp,
        attack: myAnimal?.atk,
        defence: myAnimal?.def,
        exchange: 1 + playerExchange / 10,
      };

      const enemyStat = {
        animal: selectedBankDetails.branchDto.managerAnimalType,
        Level: selectedBankDetails?.branchDto.managerLevel,
        exp: selectedBankDetails.branchDto.managerExp,
        Hp: selectedBankDetails?.branchDto.managerHp,
        attack: selectedBankDetails?.branchDto.managerAtk,
        defence: selectedBankDetails?.branchDto.managerDef,
        exchange: 1 + enemyExchange / 10,
      };
      dispatch(setEnemy(enemyStat));
      dispatch(setPlayer(playerStat));
      dispatch(setMaxHpBar("player", playerStat.Hp));
      dispatch(setMaxHpBar("enemy", enemyStat.Hp));
      const randomNum = Math.floor(Math.random() * 9) + 1; // 배틀 배경 랜덤값 설정
      navigation.navigate("Fight", {
        screen: "MainBattle",
        params: { randomNum: randomNum },
      });
    }
  };

  return (
    <View style={styles.screen}>
      <MapView
        style={styles.map}
        showsUserLocation={true}
        showsMyLocationButton={true}
        loadingEnabled
        region={region}
        provider={PROVIDER_GOOGLE} // 여기까지 전체 구글맵 보여주기용
      >
        {banks &&
          banks.map(
            (
              bank,
              index // 은행 지점별 정보
            ) => (
              <Marker
                key={index}
                coordinate={{
                  latitude: bank.지점위도,
                  longitude: bank.지점경도,
                }}
                title={bank.지점명}
                description={bank.지점주소}
                onPress={() => handleMarkerPress(bank)}
              />
            )
          )}
      </MapView>
      <Modal
        animationType="slide" // 모달창
        transparent={true}
        visible={isModalVisible}
        onRequestClose={closeModal}
      >
        <TouchableOpacity
          style={styles.modalBack}
          activeOpacity={1}
          onPressOut={closeModal}
        >
          <View
            style={styles.modalContent}
            onStartShouldSetResponder={() => true}
          >
            <View style={styles.modalIn}>
              <Text style={styles.modalText}>
                LV. {selectedBankDetails?.branchDto.managerLevel}_
                {selectedBankDetails?.username}
              </Text>
              {selectedBankDetails && (
                <Image
                  source={
                    images.animal[
                      `baby_${selectedBankDetails.branchDto.managerAnimalType}`
                    ]
                  }
                  style={styles.modalImage}
                />
              )}
              <Text style={styles.modalText}>
                HP : {selectedBankDetails?.branchDto.managerHp}
              </Text>
              <Text style={styles.modalText}>
                ATK : {selectedBankDetails?.branchDto.managerAtk}
              </Text>
              <Text style={styles.modalText}>
                DEF : {selectedBankDetails?.branchDto.managerDef}
              </Text>
              <TouchableOpacity
                onPress={handleGoToBattle}
                style={styles.battleButton}
              >
                <Text>배틀로 이동</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={closeModal} style={styles.closeButton}>
                <Text>닫기</Text>
              </TouchableOpacity>
            </View>
          </View>
        </TouchableOpacity>
      </Modal>
    </View>
  );
};

export default Map;

const styles = StyleSheet.create({
  screen: {
    flex: 1,
  },
  map: {
    width: "100%",
    height: "100%",
  },
  modalBack: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0,0,0,0.7)",
  },
  modalContent: {
    width: ScreenWidth * 0.8,
    height: ScreenHeight * 0.5,
    backgroundColor: "#2c2c2c",
    padding: 20,
    justifyContent: "center",
    alignItems: "center",
    borderColor: "#ffd700",
    borderWidth: 4,
    borderRadius: 15,
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
    // backgroundColor: "yellow"
  },
  modalIn: {
    width: ScreenWidth * 0.72,
    height: ScreenHeight * 0.45,
    alignItems: "center",
    // backgroundColor: "green"
  },
  modalText: {
    color: "#ffd700",
    fontSize: 20,
    fontWeight: "bold",
    marginBottom: 10,
  },
  battleButton: {
    width: ScreenWidth * 0.648,
    marginTop: 20,
    padding: 10,
    backgroundColor: "#007BFF",
    borderColor: "#2c2c2c",
    borderWidth: 2,
    borderRadius: 5,
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  closeButton: {
    width: ScreenWidth * 0.648,
    marginTop: 20,
    padding: 10,
    backgroundColor: "#ffd700",
    borderColor: "#2c2c2c",
    borderWidth: 2,
    borderRadius: 5,
    alignItems: "center",
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
  modalImage: {
    width: ScreenWidth * 0.4,
    height: ScreenHeight * 0.22,
    marginBottom: 20,
  },
});
