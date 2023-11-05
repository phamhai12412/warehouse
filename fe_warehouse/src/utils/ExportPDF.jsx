import { Cookies } from "react-cookie";
import instance from "../redux/api";

export const exportPDF = async (api, fileName) => {
  try {
    const resp = await instance.get(api, {
      headers: {
        Authorization: `Bearer ${new Cookies().get('token')}`
      },
      responseType: "blob"
    });
    const url_2 = window.URL.createObjectURL(new Blob([resp.data]));
    const a = document.createElement('a');
    a.href = url_2;
    a.download = fileName + '.pdf';
    a.click();
    window.URL.revokeObjectURL(url_2);
  } catch (err) {
    return console.log(err);
  }
}