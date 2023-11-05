import { Button, Chip, Table, TableBody, TableCell, TableHead, TableRow, Tooltip } from '@mui/material'
import { GET_ALL_BILL, REJECT_BILL } from '../../redux/api/service/billService';
import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';

import BillConfirm from '../modal/BillConfirm';
import CheckIcon from '@mui/icons-material/Check';
import { DATA_BILL_REMOTE } from '../../redux/selectors/selectors';
import DeleteIcon from '@mui/icons-material/Delete';
import { Toaster } from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

function BillPendingAdmin({ reload, handleReload }) {

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const bills = useSelector(DATA_BILL_REMOTE);

  const [detail, setDetail] = useState(null);


  const [openConfirm, setOpenConfirm] = useState(false);
  const handleOpenConfirm = () => setOpenConfirm(true);
  const handleCloseConfirm = () => setOpenConfirm(false);

  // start handle status bill
  const handlePrepare = (element) => {
    setDetail(element);
    handleOpenConfirm();
  }

  const handleDelete = (id) => {
    dispatch(REJECT_BILL(id));
    setTimeout(() => {
      handleReload();
    }, 200)
  }

  useEffect(() => {
    dispatch(GET_ALL_BILL({ filter: "ALL", search: "" }));
  }, [reload])

  return (
    <div className='bg-white'>
      {detail &&
        <BillConfirm
          open={openConfirm}
          handleClose={handleCloseConfirm}
          detail={detail}
          handleReload={handleReload}
        />
      }
      <div className='w-full px-5 py-3'>
        <h1 className='uppercase font-semibold text-lg'>Phiếu Chờ Xét Duyệt</h1>
      </div>
      <Table sx={{ width: '100%' }} aria-label="simple table">
        <TableHead>
          <TableRow className='bg-slate-300'>
            <TableCell align="center">STT</TableCell>
            <TableCell align='center'>NGÀY TẠO</TableCell>
            <TableCell align="center">ĐIỂM BẮT ĐẦU</TableCell>
            <TableCell align='center'>ĐIỂM KẾT THÚC</TableCell>
            <TableCell align='center'>ĐƠN VỊ GIAO HÀNG</TableCell>
            <TableCell align='center'>TỔNG TIỀN</TableCell>
            <TableCell align='center'>HÀNH ĐỘNG</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {bills && bills.map((e, index) => {
            if (e?.delivery === "PENDING") {
              return (
                <TableRow
                  key={e?.bill.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                  className='hover:bg-slate-100 transition-all'
                >
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{index + 1}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{e?.bill.created} </TableCell>
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{e?.bill.locationStart} </TableCell>
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{e?.bill.locationEnd}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{e?.bill.shipment.shipName}</TableCell>
                  <TableCell onClick={() => navigate(`/admin/bills`)} align='center'>{(e?.bill.total + e?.bill.priceShip).toLocaleString()}</TableCell>
                  <TableCell align='center'>
                    <div className='flex gap-2 justify-center'>
                      <Button variant="contained" onClick={() => handlePrepare(e)} color='warning'><Tooltip title="Chấp Thuận"><CheckIcon /></Tooltip></Button>
                      <Button variant="contained" onClick={() => handleDelete(e?.bill.id)} color='error'><Tooltip title='Từ Chối'><DeleteIcon /></Tooltip></Button>
                    </div>
                  </TableCell>
                </TableRow>
              )
            }
          })}
        </TableBody>
      </Table>
      <Toaster />
    </div>
  )
}

export default BillPendingAdmin