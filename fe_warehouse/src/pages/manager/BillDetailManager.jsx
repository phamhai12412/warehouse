import { Button, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import DescriptionIcon from '@mui/icons-material/Description';
import PersonIcon from '@mui/icons-material/Person';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import { exportPDF } from '../../utils/ExportPDF';
import instance from '../../redux/api';

function BillDetailManager() {

  const { id } = useParams();

  const navigate = useNavigate();

  const [data, setData] = useState(null);

  const [user, setUser] = useState(JSON.parse(localStorage.getItem("user")));

  const handleLoadBill = () => {
    instance.get(`/api/v1/shipping-report/bill/${id}`)
      .then(resp => setData(resp.data))
      .catch(err => console.log(err))
  }

  const handleGoBack = () => {
    if (user) {
      if (data.start.id == user.storageId) {
        navigate('/manager/bills/export');
      } else {
        navigate('/manager/bills/import');
      }
    } else {
      navigate('/');
    }


  }

  const handleExportPDF = () => {
    if (data.start.id === user.storageId) {
      exportPDF(`/api/v1/shipping-report/${id}/export`, "export_bill");
    } else {
      exportPDF(`/api/v1/shipping-report/${id}/import`, "import_bill");
    }
  }

  useEffect(() => {
    handleLoadBill();
  }, [])

  return (
    <div className='mx-36'>
      <div className="service flex gap-6">
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className="text-xl text-center">Nơi Gửi Hàng</h2>
          <div className='p-3'>
            {data &&
              <div>
                <div className="info_user border-b-2 border-black pb-2">
                  <h2 className='text-lg font-semibold flex items-center justify-between'>
                    <span><PersonIcon className='mr-1' /> Thông tin người gửi: </span><p></p>
                  </h2>
                  <p className='text-lg font-semibold text-blue-600 '>
                    {
                      data.start.users.sex ? <>Anh {data.start.users.firstName + " " + data.start.users.lastName}</> :
                        <>Chị {data.start.users.firstName + " " + data.start.users.lastName}</>
                    }<span className='text-black'>{" - " + data.start.users.phone + " "} </span>
                  </p>
                </div>
                <div className="info_storae">
                  <h2 className='text-lg font-semibold mt-2 flex items-center'>
                    <WarehouseIcon className='mr-2' />{data.start.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                  </h2>
                  <ul className='mt-3'>
                    <li><ArrowRightIcon /> Số Điện Thoại : {data.start.users.phone}</li>
                    <li><ArrowRightIcon /> Email : {data.start.users.email}</li>
                    <li><ArrowRightIcon /> Địa Chỉ : {data.start.address.toUpperCase()}</li>
                    <li><ArrowRightIcon /> Khu Vực : {data.start.zone.zoneName}</li>
                  </ul>
                </div>
              </div>
            }
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Nơi Nhận Hàng</h2>
          <div className='p-3'>
            {data &&
              <div>
                <div className="info_user border-b-2 border-black pb-2">
                  <h2 className='text-lg font-semibold flex items-center justify-between'>
                    <span><PersonIcon className='mr-1' /> Thông tin người nhận: </span><p></p>
                  </h2>
                  <p className='text-lg font-semibold text-blue-600 '>
                    {
                      data.end.users.sex ? <>Anh {data.end.users.firstName + " " + data.end.users.lastName}</> :
                        <>Chị {data.end.users.firstName + " " + data.end.users.lastName}</>
                    }<span className='text-black'>{" - " + data.end.users.phone + " "} </span>
                  </p>
                </div>
                <div className="info_storae">
                  <h2 className='text-lg font-semibold mt-2 flex items-center'>
                    <WarehouseIcon className='mr-2' />{data.end.typeStorage === "STORAGE" ? 'Thông tin kho hàng:' : 'Thông tin siêu thị:'}
                  </h2>
                  <ul className='mt-3'>
                    <li><ArrowRightIcon /> Số Điện Thoại : {data.end.users.phone}</li>
                    <li><ArrowRightIcon /> Email : {data.end.users.email}</li>
                    <li><ArrowRightIcon /> Địa Chỉ : {data.end.address.toUpperCase()}</li>
                    <li><ArrowRightIcon /> Khu Vực : {data.end.zone.zoneName}</li>
                  </ul>
                </div>
              </div>
            }
          </div>
        </div>
        <div className='flex-1 bg-white p-5 rounded-md'>
          <h2 className='text-xl text-center'>Đơn Vị Giao Hàng</h2>
          <div className='flex py-3 justify-center'>
            {data &&
              <div className='text-center'>
                <p><span className='text-lg font-semibold'>Đơn vị vận chuyển :</span> {data.bill.shipment.shipName.toUpperCase()}</p>
                <p><span className='text-lg font-semibold'>Giá Tiền :</span> {data.bill.shipment.price.toLocaleString()} VND / kg</p>
              </div>
            }
          </div>
        </div>
      </div>
      <div className="bill bg-white mt-6 p-5">
        <h2 className='text-lg font-semibold flex items-center gap-2'><DescriptionIcon /> <span>Thông tin chi tiết đơn hàng</span></h2>
      </div>
      {/* nơi hiển thị chi tiết những sản phẩm trong đơn hàng */}
      <div className="bill_detail bg-white border-t-2 border-slate-300">
        <TableContainer component={Paper} >
          <Table sx={{ width: '100%' }} aria-label="simple table">
            <TableHead sx={{ bgcolor: '#ecf0f1' }}>
              <TableRow>
                <TableCell align='center'>STT</TableCell>
                <TableCell align='left'>MÃ SẢN PHẨM</TableCell>
                <TableCell align='left'>TÊN</TableCell>
                <TableCell align='center'>SỐ LƯỢNG</TableCell>
                <TableCell align='center'>GIÁ THÀNH</TableCell>
                <TableCell align='center'>TỔNG TIỀN</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {
                data && data.billDetail.map((item, index) => {
                  return (
                    <TableRow key={item.id}>
                      <TableCell align='center'>{index + 1}</TableCell>
                      <TableCell align='left'>{item.product.code}</TableCell>
                      <TableCell align='left'>{item.product.productName}</TableCell>
                      <TableCell align='center'><p>{item.quantity}</p></TableCell>
                      <TableCell align='center'>{item.product.price.toLocaleString()} đ</TableCell>
                      <TableCell align='center'>{item.total.toLocaleString()} đ</TableCell>
                    </TableRow>
                  )
                })
              }
            </TableBody>
          </Table>
        </TableContainer>
      </div>
      {/* nơi thông tin tổng tiền */}
      <div className="total_detail bg-white flex justify-end p-5">
        <div className='w-96'>
          <ul className='flex flex-col gap-2'>
            <li className='flex justify-between font-normal'><span>Tổng sản phẩm : </span><span>{data?.billDetail.reduce((sum, item) => { return sum += item.quantity }, 0)}</span></li>
            <li className='flex justify-between font-normal'><span>Tổng giá tiền : </span><span>{data?.bill.total.toLocaleString()} đ</span></li>
            <li className='flex justify-between font-normal'><span>Phí ship hàng : </span><span>{data && (data?.billDetail.reduce((sum, item) => { return sum += item.product.weight * item.quantity }, 0) * data?.bill.shipment.price).toLocaleString()} VND / kg</span></li>
          </ul>
        </div>
      </div>
      <div className="action flex justify-end py-3 gap-3">
        <Button variant="outlined" onClick={handleExportPDF} className='flex items-center gap-2'><DescriptionIcon /> <span>Xuất File PDF</span></Button>
        <Button variant="contained" onClick={handleGoBack}>Quay lại</Button>
      </div>
    </div >
  )
}

export default BillDetailManager