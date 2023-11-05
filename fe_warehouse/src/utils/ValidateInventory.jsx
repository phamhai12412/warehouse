
export const validateInventoryDate = (inventories) => {
  let date = new Date();
  for (let i = 0; i < inventories.length; i++) {
    if (getYear(inventories[i].created) === (date.getFullYear() + "")) {
      if (getMonth(inventories[i].created) === (date.getMonth() + 1 + "")) {
        if (getDay(inventories[i].created) === (date.getDate() + "")) {
          return true;
        }
      }
    }
  }
  return false;
}

// json yyyy-mm-dd
const getDay = (date) => {
  let arr = date.split("-");
  return arr[2];
}

const getMonth = (date) => {
  let arr = date.split("-");
  return arr[1];
}

const getYear = (date) => {
  let arr = date.split("-");
  return arr[0];
}