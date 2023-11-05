export const validateTime = (time) => {
  let now = new Date();
  if (now.getFullYear().toString() === getYear(time)) {
    if ((now.getMonth() + 1).toString() === getMonth(time)) {
      if (now.getDate().toString() === getDay(time)) {
        return true;
      }
    }
  }
  return false;
}

// json yyyy-MM-dd
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