import { Backdrop, Box, Chip, Fade, Modal, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material'

import React from 'react'

function InventoryDetail({ openInventory, handleCloseInventory, userSend }) {

  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    bgcolor: 'background.paper',
    boxShadow: 24,
    // p: 4,
    width: '1500px'
  };

  return (
    <Modal
      aria-labelledby="transition-modal-title"
      aria-describedby="transition-modal-description"
      open={openInventory}
      onClose={handleCloseInventory}
      closeAfterTransition
      slots={{ backdrop: Backdrop }}
      slotProps={{
        backdrop: {
          timeout: 500,
        },
      }}
    >
      <Fade in={openInventory}>
        <Box sx={style}>
          {/* <TableContainer component={Paper} > */}
          <Table sx={{ width: '100%' }} aria-label="simple table">
            <TableHead sx={{ bgcolor: '#ecf0f1' }}>
              <TableRow>
                <TableCell align='center'>STT</TableCell>
                <TableCell align='left' sx={{ width: '150px' }}>Tên sản phẩm</TableCell>
                <TableCell align='left'>Mã sản phẩm</TableCell>
                <TableCell align='left'>Ngày sản xuất</TableCell>
                <TableCell align='left'>Ngày hết hạn</TableCell>
                <TableCell align='left'>Thể Loại</TableCell>
                <TableCell align='center'>số lượng</TableCell>
                <TableCell align='center'>cân nặng</TableCell>
                <TableCell align='center'>giá tiền</TableCell>
                <TableCell align='center'>Trạng thái</TableCell>
                <TableCell align='center'>Tình Trạng</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {
                userSend.productResponseList && userSend.productResponseList.map((item, index) => {
                  if (item.statusName !== "PENDING") {
                    if (item.quantity === 0) {
                      return (
                        <TableRow key={item.id}>
                          <TableCell align='center'>{index + 1}</TableCell>
                          <TableCell align='left'>{item.productName}</TableCell>
                          <TableCell align='left'>{item.code}</TableCell>
                          <TableCell align='left'>{item.createdDate}</TableCell>
                          <TableCell align='left'>{item.dueDate}</TableCell>
                          <TableCell align='left'>{item.category.categoryName.toUpperCase()}</TableCell>
                          <TableCell align='center'>{item.quantity}</TableCell>
                          <TableCell align='center'>{item.weight}</TableCell>
                          <TableCell align='center'>{item.price.toLocaleString()} đ</TableCell>
                          <TableCell align='center'><Chip label="Hết hàng" color="error" /></TableCell>
                          <TableCell align='center'><Chip label="Không Bán" color="error" /></TableCell>
                        </TableRow>
                      )
                    } else {
                      return (
                        <TableRow key={item.id}>
                          <TableCell align='center'>{index + 1}</TableCell>
                          <TableCell align='left'>{item.productName}</TableCell>
                          <TableCell align='left'>{item.code}</TableCell>
                          <TableCell align='left'>{item.createdDate}</TableCell>
                          <TableCell align='left'>{item.dueDate}</TableCell>
                          <TableCell align='left'>{item.category.categoryName.toUpperCase()}</TableCell>
                          <TableCell align='center'>{item.quantity}</TableCell>
                          <TableCell align='center'>{item.weight}</TableCell>
                          <TableCell align='center'>{item.price.toLocaleString()} đ</TableCell>
                          <TableCell align='center'><Chip label="Còn hàng" color="success" /></TableCell>
                          <TableCell align='center'>
                            {item.statusName === "DELETE" ? <Chip label="Không Bán" color="error" /> : <Chip label="Còn Bán" color="success" />}
                          </TableCell>
                        </TableRow>
                      )
                    }
                  }
                })
              }
            </TableBody>
          </Table>
          {/* </TableContainer> */}
        </Box>
      </Fade>
    </Modal>
  )
}

export default InventoryDetail