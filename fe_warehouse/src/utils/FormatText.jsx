
export const formatText = (text) => {
  let arr = text.split(' ');
  let result = '';
  console.log("arr =>", arr);
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] !== '') {
      result += arr[i] + " ";
    }
  }
  return result.trim();
}