import { Box, Modal, Table, TableBody, TableCell, TableHead, TableRow } from '@mui/material';
import React, { useEffect, useState } from 'react'

import instance from '../../redux/api';

function ProductByCategory({ openShow, handleCloseShow, categoryId }) {

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    bgcolor: 'background.paper',
    boxShadow: 24,
    // p: 4,
  };

  const [data, setData] = useState(null);

  const handleLoadProductByCategory = () => {
    instance.get(`/api/v1/products/category/${categoryId}`)
      .then(resp => setData(resp.data))
      .catch(err => console.log(err))
  }

  useEffect(() => {
    handleLoadProductByCategory();
  }, [])

  return (
    <Modal
      open={openShow}
      onClose={handleCloseShow}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Table sx={{ width: '100%' }} aria-label="simple table">
          <TableHead sx={{ bgcolor: '#ecf0f1' }}>
            <TableRow>
              <TableCell align='center'>STT</TableCell>
              <TableCell align='left' sx={{ width: '150px' }}>Tên sản phẩm</TableCell>
              <TableCell align='left'>Mã sản phẩm</TableCell>
              <TableCell align='left'>Ngày sản xuất</TableCell>
              <TableCell align='left'>Ngày hết hạn</TableCell>
              <TableCell align='center'>Thể Loại</TableCell>
              <TableCell align='center'>số lượng</TableCell>
              <TableCell align='center'>giá tiền</TableCell>
              <TableCell align='center'>Kho</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {
              data && data.map((item, index) => {
                return (
                  <TableRow key={item.id}>
                    <TableCell align='center'>{index + 1}</TableCell>
                    <TableCell align='left'>{item.productName}</TableCell>
                    <TableCell align='left'>{item.code}</TableCell>
                    <TableCell align='left'>{item.createdDate}</TableCell>
                    <TableCell align='left'>{item.dueDate}</TableCell>
                    <TableCell align='center'>{item.category.categoryName}</TableCell>
                    <TableCell align='center'>{item.quantity}</TableCell>
                    <TableCell align='center'>{item.price.toLocaleString()} đ</TableCell>
                    <TableCell align='center'>{item.storage.address.toUpperCase()}</TableCell>
                  </TableRow>
                )
              })
            }
          </TableBody>
        </Table>
      </Box>
    </Modal>
  )
}

export default ProductByCategory