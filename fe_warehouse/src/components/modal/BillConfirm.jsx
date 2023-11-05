import React, { useEffect, useState } from 'react';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';

import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import Button from '@mui/material/Button';
import { Cookies } from 'react-cookie';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import PersonIcon from '@mui/icons-material/Person';
import Swal from 'sweetalert2';
import WarehouseIcon from '@mui/icons-material/Warehouse';
import instance from '../../redux/api';
import { useNavigate } from 'react-router-dom';

function BillConfirm({ open, handleClose, detail, handleReload }) {

  const navigate = useNavigate();

  const [data, setData] = useState(null);

  const handlePrepare = async () => {
    await instance.put(`/api/v1/shipping-report/approveBill/${detail.bill.id}`, {}, {
      headers: {
        Authorization: `Bearer ${new Cookies().get("token")}`
      }
    }).then((resp) => { navigate('/admin/bills'); })
      .catch((err) => {
        Swal.fire({
          title: 'Error',
          text: 'Không đủ số lượng vui lòng bổ sung số lượng!',
          icon: 'error',
          showCloseButton: true,
          cancelButtonColor: '#27ae60',
          cancelButtonText: 'OK',
        }).then((result) => {
          handleClose();
        })
      });

    setTimeout(() => {
      handleReload();
    }, 200)
    handleClose();
  }

  const handleLoadBill = () => {
    instance.get(`/api/v1/shipping-report/bill/${detail.bill.id}`)
      .then(resp => setData(resp.data))
      .catch(err => console.log(err))
  }

  useEffect(() => {
    handleLoadBill();
  }, [])

  return (
    <div>
      <Dialog
        open={open}
        onClose={handleClose}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        fullWidth
      >
        <DialogTitle id="alert-dialog-title">
          <p className='text-red-500 text-center uppercase font-semibold'>! Hãy Xác Nhận Đơn !</p>
        </DialogTitle>
        <DialogContent>
          <div>
            <div className='flex mb-3 gap-5'>
              <div className='flex-1'>
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
                        <li><ArrowRightIcon /> Địa Chỉ : {data.start.address}</li>
                        <li><ArrowRightIcon /> Khu Vực : {data.start.zone.zoneName}</li>
                      </ul>
                    </div>
                  </div>
                }
              </div>
              <div className='flex-1'>
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
                        <li><ArrowRightIcon /> Địa Chỉ : {data.end.address}</li>
                        <li><ArrowRightIcon /> Khu Vực : {data.end.zone.zoneName}</li>
                      </ul>
                    </div>
                  </div>
                }
              </div>
            </div>
            <div>
              <Table sx={{ width: '100%' }} aria-label="simple table">
                <TableHead sx={{ bgcolor: '#ecf0f1' }}>
                  <TableRow>
                    <TableCell align='center'>STT</TableCell>
                    <TableCell align='left'>Tên sản phẩm</TableCell>
                    <TableCell align='center'>Thể Loại</TableCell>
                    <TableCell align='center'>số lượng</TableCell>
                    <TableCell align='center'>giá tiền</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {
                    data && data.billDetail.map((item, index) => {
                      return (
                        <TableRow className='hover:bg-slate-100' key={item.id}>
                          <TableCell align='center'>{index + 1}</TableCell>
                          <TableCell align='left'>{item.product.productName}</TableCell>
                          <TableCell align='center'>{item.product.category.categoryName}</TableCell>
                          <TableCell align='center'>{item.quantity}</TableCell>
                          <TableCell align='center'>{item.product.price.toLocaleString()}</TableCell>
                        </TableRow>
                      )
                    })
                  }
                </TableBody>
              </Table>
            </div>
          </div>
        </DialogContent>
        <DialogActions sx={{ display: 'flex', justifyContent: 'center' }}>
          <Button variant='contained' color='error' onClick={handleClose}>Close</Button>
          <Button variant='contained' color='success' onClick={handlePrepare}>Agree</Button>
        </DialogActions>
      </Dialog>
    </div>
  )
}

export default BillConfirm