import React, { useEffect, useState } from 'react'
import { TextField, Tooltip } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';

import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import BarChartExport from '../../components/dashboard/BarChartExport'
import BarChartImport from '../../components/dashboard/BarChartImport'
import { Cookies } from 'react-cookie';
import { DATA_STORAGE } from '../../redux/selectors/selectors';
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { DemoContainer } from "@mui/x-date-pickers/internals/demo";
import { GET_ALL_STORAGE } from '../../redux/api/service/storageService';
import HelpRecipient from '../../components/help/HelpRecipient';
import LoadingComponent from '../../components/loading/LoadingComponent';
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import instance from '../../redux/api';

function ChartOverviewBill() {

  const [loading, setLoading] = useState(true);

  const storage = useSelector(DATA_STORAGE);
  const dispatch = useDispatch();

  const [dataExport, setDataExport] = useState(null);
  const [dataImport, setDataImport] = useState(null);

  const [storageId, setStorageId] = useState(null);
  const [yearValue, setYearValue] = useState((new Date()).getFullYear());

  const [searchStorage, setSearchStorage] = useState('');
  const handleChangeSearch = (e) => setSearchStorage(e.target.value);

  const [storageName, setStorageName] = useState('');

  const handleSelectStorageId = (item) => {
    setLoading(true);

    setSearchStorage("");
    setStorageId(item.id);
    setStorageName(item.storageName);
  }

  const handleChangeYearValue = (e) => {
    setYearValue(e.$y);
  }

  const handleLoadDataExport = () => {
    instance.get(`/api/v1/dashboard/dashboard-export/${storageId}/?yearValue=${yearValue}`, {
      headers: {
        Authorization: `Bearer ${new Cookies().get('token')}`
      }
    })
      .then(resp => setDataExport(resp.data.map(item => ({ name: item.name, "đơn thành công": item.sc, "đơn hủy": item.cc }))))
      .catch(err => console.log(err))
  }
  const handleLoadDataImport = () => {
    instance.get(`/api/v1/dashboard/dashboard-import/${storageId}/?yearValue=${yearValue}`, {
      headers: {
        Authorization: `Bearer ${new Cookies().get('token')}`
      }
    })
      .then(resp => setDataImport(resp.data.map(item => ({ name: item.name, "đơn thành công": item.sc, "đơn hủy": item.cc }))))
      .catch(err => console.log(err))
  }



  useEffect(() => {
    dispatch(GET_ALL_STORAGE(searchStorage));
    handleLoadDataExport();
    handleLoadDataImport();
    if (storageId) {
      setTimeout(() => {
        setLoading(false);
      }, 800)
    }
  }, [storageId, yearValue, searchStorage])

  return (
    <div>
      <div className='flex justify-center text-3xl font-semibold uppercase py-5'>báo cáo đơn hàng theo từng kho</div>
      <div className='pl-10'>
        <div className="info text-center py-5 text-xl uppercase">{storageName ? "Kho : " + storageName : "Vui Lòng Chọn Kho"}</div>
        <div className='bg-white p-5 rounded-md flex items-end gap-3 mr-10'>

          <div className='flex-1 relative' >
            <TextField fullWidth value={searchStorage} id="outlined-basic" label={searchStorage ? searchStorage : "Tìm kiếm theo tên kho"} variant="outlined" onChange={handleChangeSearch} />
            <Tooltip title={<HelpRecipient />}><QuestionMarkIcon className='absolute right-3 top-1/2 -translate-y-1/2 hover:cursor-pointer' /></Tooltip>
            {
              searchStorage &&
              <div className='absolute z-10 w-full mt-2 bg-slate-200 p-1 shadow-2xl'>
                {storage.map(item => {
                  return <p key={item.id} onClick={() => handleSelectStorageId(item)} className='p-2 m-2 bg-white shadow-md hover:cursor-pointer'>{item.storageName}</p>
                })}
              </div>
            }
          </div>
          <LocalizationProvider dateAdapter={AdapterDayjs} >
            <DemoContainer components={["DatePicker"]}>
              <DatePicker label={yearValue} views={["year"]} onChange={handleChangeYearValue} />
            </DemoContainer>
          </LocalizationProvider>
        </div>
      </div>
      <div className='flex flex-col gap-5 px-10 py-10'>
        {storageId && !loading ? <>
          <BarChartExport dataExport={dataExport} />
          <BarChartImport dataImport={dataImport} />
        </> :
          (!storageId &&
            <div className='w-full bg-white h-96 flex justify-center items-center'>
              <h1 className='text-3xl font-semibold text-slate-500'>Vui Lòng Chọn Kho</h1>
            </div>
          )
        }
      </div>
      {loading && storageId && <div className='px-10'><LoadingComponent /></div>}
    </div >
  )
}

export default ChartOverviewBill